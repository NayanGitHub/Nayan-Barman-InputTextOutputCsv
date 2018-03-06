package com.nayan.main;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.log4j.Logger;

import com.nayan.util.FileUtil;
import com.nayan.util.StringUtil;

import java.io.*;

public class InputTxtOutputCsv {
	private static final Logger logger = Logger.getLogger(InputTxtOutputCsv.class);
	private static final String reportDir ="C:\\Users\\nayan.barman\\Google Drive\\Job_Search_Code\\Output File\\";
	private static final String inputFileDir = "C:\\Users\\nayan.barman\\Google Drive\\Job_Search_Code\\Input File\\";

	public void getOutputCsv() {
		
		String filename = inputFileDir+"input.txt";
		
		try{
		File file = new File(filename);
		Scanner inputFile = new Scanner(file);
		
		int count = 1;
		List<ResultDetail> resultDetailsList = new ArrayList<ResultDetail>();
		
		while (inputFile.hasNext()) {
			
			String inputLine = inputFile.nextLine();
			logger.info("RECORD NUMBER "+count++);
			String client_Info = inputLine.substring(3, 7) + inputLine.substring(7, 11) + inputLine.substring(11, 15)
					+ inputLine.substring(15, 19);
			logger.info("Client Information "+client_Info);

			String product_Info = inputLine.substring(27, 31) + inputLine.substring(25, 27)
					+ inputLine.substring(31, 37) + inputLine.substring(37, 45);
			logger.info("Product Information "+product_Info);
			
			int total_trnx_amt_int = Integer.parseInt(inputLine.substring(52, 62)) - Integer.parseInt(inputLine.substring(63, 73));
			String total_trnx_amount = String.valueOf(total_trnx_amt_int);
			logger.info("Total Transaction Amount" + total_trnx_amount + "  ");
			
			resultDetailsList.add(new ResultDetail(client_Info, product_Info, total_trnx_amount));
			generateReport(resultDetailsList);
		
		}

		inputFile.close();
		}
		catch(IOException ioE){
			logger.error("IOException");
		}
		catch(Exception ioE){
			logger.error("Exception");
		}
	}
	
	private void generateReport(List<ResultDetail> reportDetails) {
        if (reportDetails == null || reportDetails.isEmpty()) {
            logger.info("no details to report");
        }

        // create report file
        File reportFile = null;
        String reportFileName = FileUtil.concatToFileName(reportDir, "Output.csv");
        try {
            File dir = new File(reportDir);
            if (!dir.exists()) {
                if (!dir.mkdir()) {
                    throw new Exception("failed to create directory " + reportDir);
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
            logger.error("Failed to create report file", e);
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
            return "Client_Information,"
                    + "Product_Information,"
                    + "Total_Transaction_Amount,";
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