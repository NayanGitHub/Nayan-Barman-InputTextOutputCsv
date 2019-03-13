package com.nayan.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import org.apache.log4j.Logger;
import com.nayan.util.*;

import java.io.*;

public class InputTxtOutputCsv {
	private static final Logger logger = Logger.getLogger(InputTxtOutputCsv.class);
	private static Properties prop=null;
	static{
        InputStream is = null;
        try {
            prop = new Properties();
            is = InputTxtOutputCsv.class.getClassLoader().getResourceAsStream("config.properties");
            prop.load(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public void getOutputCsv() {
		
		String filename = prop.getProperty("inputFileDir") + prop.getProperty("inputFileName");
		
		try{
		File file = new File(filename);
		Scanner inputFile = new Scanner(file);
		
		int count = 1;
		List<ResultDetail> resultDetailsList = new ArrayList<ResultDetail>();
		
		while (inputFile.hasNext()) {
			
			String inputLine = inputFile.nextLine();
			logger.info(prop.getProperty("rec_num")+count++);
			String client_type=inputLine.substring(FieldNamePos.CLIENT_TYPE_START.getFieldNamePos()-1, FieldNamePos.CLIENT_TYPE_END.getFieldNamePos());
			String client_number=inputLine.substring(FieldNamePos.CLIENT_NUMBER_START.getFieldNamePos()-1, FieldNamePos.CLIENT_NUMBER_END.getFieldNamePos());
			String account_number=inputLine.substring(FieldNamePos.ACCOUNT_NUMBER_START.getFieldNamePos()-1, FieldNamePos.ACCOUNT_NUMBER_END.getFieldNamePos());
			String subaccount_number=inputLine.substring(FieldNamePos.SUBACCOUNT_NUMBER_START.getFieldNamePos()-1, FieldNamePos.SUBACCOUNT_NUMBER_END.getFieldNamePos());
			
			String client_Info = String.format("%s%s%s%s",client_type,client_number,account_number, subaccount_number);
			 
			logger.info(prop.getProperty("Client_Information")+" "+client_Info);
			
			String exchange_code=inputLine.substring(FieldNamePos.EXCHANGE_CODE_START.getFieldNamePos()-1, FieldNamePos.EXCHANGE_CODE_END.getFieldNamePos());
			String product_group_code=inputLine.substring(FieldNamePos.PRODUCT_GROUP_CODE_START.getFieldNamePos()-1, FieldNamePos.PRODUCT_GROUP_CODE_END.getFieldNamePos());
			String symbol=inputLine.substring(FieldNamePos.SYMBOL_START.getFieldNamePos()-1, FieldNamePos.SYMBOL_END.getFieldNamePos());
			String expiration_date=inputLine.substring(FieldNamePos.EXPIRATION_DATE_START.getFieldNamePos()-1, FieldNamePos.EXPIRATION_DATE_END.getFieldNamePos());

			String product_Info = String.format("%s%s%s%s",exchange_code,product_group_code,symbol, expiration_date);
			
			logger.info(prop.getProperty("Product_Information")+" "+product_Info);
			
			String quantity_long=inputLine.substring(FieldNamePos.QUANTITY_LONG_START.getFieldNamePos()-1, FieldNamePos.QUANTITY_LONG_END.getFieldNamePos());
			String quantity_short=inputLine.substring(FieldNamePos.QUANTITY_SHORT_START.getFieldNamePos()-1, FieldNamePos.QUANTITY_SHORT_END.getFieldNamePos());
			
			int total_trnx_amt_int = Integer.parseInt(quantity_long) - Integer.parseInt(quantity_short);
			String total_trnx_amount = String.valueOf(total_trnx_amt_int);
			
			logger.info(prop.getProperty("Total_Transaction_Amount")+" " + total_trnx_amount + "  ");
			
			resultDetailsList.add(new ResultDetail(client_Info, product_Info, total_trnx_amount));
			generateReport(resultDetailsList);
		
		}
		inputFile.close();
		}
		catch(IOException ioE){
			logger.error("IOException");
		}
		catch(Exception Ex){
			logger.error("Exception");
		}
	}
	
	private void generateReport(List<ResultDetail> reportDetails) {
        if (reportDetails == null || reportDetails.isEmpty()) {
            logger.info(prop.getProperty("no_report_details"));
        }

        // create report file
        File reportFile = null;
        String reportFileName = FileUtil.concatToFileName(prop.getProperty("reportDir"), prop.getProperty("outputFileName"));
        try {
            File dir = new File(prop.getProperty("reportDir"));
            if (!dir.exists()) {
                if (!dir.mkdir()) {
                    throw new Exception(prop.getProperty("fail_to_create_dir") + prop.getProperty("reportDir"));
                }
            }

            reportFile = new File(reportFileName);
            reportFile.createNewFile();
            try (BufferedWriter output = new BufferedWriter(new FileWriter(reportFile))) {

                output.append(ResultDetail.getCVSHeader());
                output.newLine();

                for (ResultDetail r : reportDetails) {
                    String row = r.getCSV();
                    //logger.info(row);
                    output.append(row);
                    output.newLine();
                }
            }

        } catch (Exception e) {
            logger.error(prop.getProperty("fail_to_create_report_file"), e);
            return;
        }
    }
	private static class ResultDetail {
    	public ResultDetail(String info_Client, String info_Product, String amount) {
    		client_Info=info_Client;
    		product_Info=info_Product;
    		total_trnx_amount=amount;
        }   	
    	
    	
    	public static String getCVSHeader() {
    		String cvsHeader = prop.getProperty("Client_Information")+","+prop.getProperty("Product_Information")+","+prop.getProperty("Total_Transaction_Amount")+",";
            return cvsHeader;
        }
    	
    	public String getCSV() throws Exception {
            return StringUtil.concatWithCommas(true, true, StringUtil.nullToEmpty(client_Info),StringUtil.nullToEmpty(product_Info),StringUtil.nullToEmpty(total_trnx_amount));
        }

        String  client_Info;
        String  product_Info;
        String  total_trnx_amount;
    }
	
	public static void main(String[] args){
		InputTxtOutputCsv mainObj= new InputTxtOutputCsv(); 
		mainObj.getOutputCsv();
	}
}