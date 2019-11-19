package com.sinaif.udaf;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.hadoop.hive.ql.exec.UDFArgumentTypeException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.parse.SemanticException;
import org.apache.hadoop.hive.ql.udf.generic.AbstractGenericUDAFResolver;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFEvaluator;
import org.apache.hadoop.hive.serde2.objectinspector.*;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorUtils;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.io.Text;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class GenericUDAFReport extends AbstractGenericUDAFResolver {

    private static final Log LOG = LogFactory.getLog(GenericUDAFReport.class.getName());

    //检查参数类型，多用
    public GenericUDAFEvaluator getEvaluator(TypeInfo[] parameters)
            throws SemanticException {
        if (parameters.length != 2) {
            throw new UDFArgumentTypeException(parameters.length ,"Exactly two argument is expected.");
        }
        return new GenericUDAFReportEvaluator();
    }



    public static class GenericUDAFReportEvaluator extends GenericUDAFEvaluator {

        // 封装接口
        StructField imeiMapField;

        StructObjectInspector map2red;

        //for PARTITAL AND COMPLETE
        private PrimitiveObjectInspector input1;
        private PrimitiveObjectInspector input2;

        // for PARTIAL2 and FINAL
        StandardMapObjectInspector imeiMapIO;

        //event
        private String[] report_step = {
                "regster","active","inter_check","machine_check","sent",
                "send_success","get_question","submit_question","open_num","line_credit",
                "audit_num","due_fee","repayment","overdue","use_money","use_auth",
                "appply_auth","card_bound_pass","face_success","mid_first_login"
        };


        //最终结果
        Text result;

        static class BusinessEventStep implements AggregationBuffer {
            Map<String,String> container;
        }

        @Override
        // 保存数据聚集结果的类
        public AggregationBuffer getNewAggregationBuffer() throws HiveException {
            BusinessEventStep result = new BusinessEventStep();
            reset(result);
            return result;
        }

        @Override
        // 重置聚集结果
        public void reset(AggregationBuffer agg) throws HiveException {

            Map step_map = new HashMap<String,String>();
            for(String step : report_step){
                step_map.put(step,"");
            }
            ((BusinessEventStep) agg).container = step_map;

        }


        @Override
        //确定各个阶段输入输出参数的数据格式ObjectInspectors
        public ObjectInspector init(Mode m, ObjectInspector[] parameters)throws HiveException {
            super.init(m, parameters);
            //map 阶段输入两个参数 sring
            if (m == Mode.PARTIAL1 || m == Mode.COMPLETE){
                input1 = (PrimitiveObjectInspector) parameters[0];
                input2 = (PrimitiveObjectInspector) parameters[1];

            } else {
                //其他阶段都为map
                imeiMapField = map2red.getStructFieldRef("imeiMap");
                imeiMapIO = (StandardMapObjectInspector) imeiMapField.getFieldObjectInspector();

            }
            if (m == Mode.PARTIAL1 || m == Mode.PARTIAL2) {
                //其他各阶段的输出都为map
                ArrayList<ObjectInspector> foi = new ArrayList<ObjectInspector>();
                ArrayList<String> fname = new ArrayList<String>();

                foi.add(ObjectInspectorFactory.getStandardMapObjectInspector(
                        PrimitiveObjectInspectorFactory.javaStringObjectInspector,
                        PrimitiveObjectInspectorFactory.javaStringObjectInspector));
                fname.add("imeiMap");
                return ObjectInspectorFactory.getStandardStructObjectInspector(fname, foi);

            } else{
                return PrimitiveObjectInspectorFactory.javaStringObjectInspector;

            }

        }

        @Override
        // map阶段，迭代处理输入sql传过来的列数据
        public void iterate(AggregationBuffer agg, Object[] parameters)
                throws HiveException {
            if(parameters.length == 0)return;
            Map eventmap = ((BusinessEventStep) agg).container;
            String event= PrimitiveObjectInspectorUtils.getString(parameters[0],input1);
            String eventtime= PrimitiveObjectInspectorUtils.getString(parameters[1],input2);
            eventmap.put(event,eventtime);
        }

        @Override
        // map与combiner结束返回结果，得到部分数据聚集结果
        public Object terminatePartial(AggregationBuffer agg) throws HiveException {
            BusinessEventStep businessEventStep = (BusinessEventStep) agg;
            Map stepmap = businessEventStep.container;
            Map partialResult = new HashMap<String,String>();
            for (Object obj : stepmap.entrySet()) {
                String key = obj.toString();
                partialResult.put(key, stepmap.get(key));
            }
            return partialResult;
        }

        @Override
        // combiner合并map返回的结果，还有reducer合并mapper或combiner返回的结果
        public void merge(AggregationBuffer agg, Object partial)throws HiveException {
            if(partial != null){
                Map stepmap = ((BusinessEventStep) agg).container;
                Map othermap = (Map<String,String>)partial;
                for (Object obj : othermap.entrySet()) {
                    String key = obj.toString();
                    stepmap.put(key, stepmap.get(key));
                }

            }

        }

        @Override
        // reducer阶段，输出最终结果
        public Object terminate(AggregationBuffer agg) throws HiveException {
            Map<String,String> stepmap = ((BusinessEventStep) agg).container;
            String str ="";
            for(int i=0; i<report_step.length; i++){
                String event = report_step[i];
                String eventtime = "";
                for (Object obj : stepmap.entrySet()) {
                    String key = obj.toString();
                    if(key == event){
                        eventtime = stepmap.get(key);
                    }
                }
                str+= (event+","+eventtime+",");
            }
            result = new Text(str.substring(0,str.length()-1));
            return result;

        }
    }


}
