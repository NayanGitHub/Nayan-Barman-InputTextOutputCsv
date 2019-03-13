# Nayan-Barman-InputTextOutputCsv

This application produces a daily summary report in CSV format called Output.csv based on the input received from the input file input.txt
The CSV has the following Headers
- Client_Information
- Product_Information
- Total_Transaction_Amount

which are derived out from the input file.

Instructions:

1. To run the application, open git bash in the desired location and execute the below steps:

    $ git clone https://github.com/NayanGitHub/Nayan-Barman-InputTextOutputCsv.git

    $ cd Nayan-Barman-InputTextOutputCsv/

    $  mvn clean install -DskipTests

2. Open the project in IDE like Eclipse by importing existing Maven Project and choosing the pom.xml of the project.

3. Execute the test case class(AllTests.java or TestCases.java) or the main class (InputTxtOutputCsv.java). 
   It will take the input file, generate and store the Output.csv file from and to the desired location as configured in the config.properties file 

4. Verify the Output.csv file if the report is as per the expectation. The input file, log file and the Output.csv file are copied to the folder 
   Nayan-Barman-InputTextOutputCsv\misc for reference.
