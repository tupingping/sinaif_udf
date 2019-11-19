package com.sinaif.udaf;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDAF;
import org.apache.hadoop.hive.ql.exec.UDAFEvaluator;
import org.apache.hadoop.hive.ql.exec.UDAFEvaluatorResolver;

import java.util.HashMap;

@Description(name = "SinaifEventInfo",
        value = "_FUNC_(col) - Example UDAF that concatenates all arguments from different rows into a single string")
public class SinaifEventInfo extends UDAF {

    @Override
    public void setResolver(UDAFEvaluatorResolver rslv) {
        super.setResolver(rslv);
    }

    @Override
    public UDAFEvaluatorResolver getResolver() {
        return super.getResolver();
    }

    public static class UDAFExampleGroupConcatEvaluator implements UDAFEvaluator {
        HashMap<String, String> data;

        //event
        static String[] report_step = {
                "active","register","firstlogin","idcard","debitcard","creditcard","personalinfo","contact","biopsy","videoauth","apply","qualification","veriface","debitcard_auth","creditcard_auth",
                "inter_check","machie_check","human_check","send","get_question","submit_question","openacc","applyloan","loan","firstloan","repeatloan","loanintraday","square_up"
        };

        public UDAFExampleGroupConcatEvaluator() {
            super();
            data = new HashMap<String, String>();
        }

        /**
         * Reset the state of the aggregation.
         */
        public void init() {

        }

        /**
         * Iterate through one row of original data.
         * <p>
         * This UDF accepts arbitrary number of String arguments, so we use
         * String[]. If it only accepts a single String, then we should use a single
         * String argument.
         * <p>
         * This function should always return true.
         */
        public boolean iterate(String parameter[]) {
            data.put(parameter[0], parameter[1]);
            return true;
        }

        /**
         * Terminate a partial aggregation and return the state.
         */
        public HashMap<String, String> terminatePartial() {
            return data;
        }

        /**
         * Merge with a partial aggregation.
         * <p>
         * This function should always have a single argument which has the same
         * type as the return value of terminatePartial().
         * <p>
         * This function should always return true.
         */
        public boolean merge(HashMap<String, String> o) {
            if (o != null) {
                for (String key : o.keySet()) {
                    data.put(key, o.get(key));
                }
            }
            return true;
        }

        /**
         * Terminates the aggregation and return the final result.
         */
        public String terminate() {

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < report_step.length; i++) {
                String event = report_step[i];
                String eventtime = "";
                if(data.containsKey(event)){
                    for (String key : data.keySet()) {
                        if (event == key) eventtime = data.get(key);
                    }
                }else{
                    event = "";
                    eventtime = "";
                }
                sb.append(event + "," + eventtime + ",");
            }
            return sb.substring(0, sb.length() - 1);
        }
    }
}
