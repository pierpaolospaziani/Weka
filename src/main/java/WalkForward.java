import java.io.*;

public class WalkForward {

    private static void writeArffLine(FileWriter fileWriter, String[] val) throws IOException {
        for(int i = 3; i < val.length; i++){
            fileWriter.append(val[i]);
            if(i != val.length-1){
                fileWriter.append(",");
            }
        }
        fileWriter.append("\n");
    }

    private static void arffInit(FileWriter wr, String name) throws IOException {
        wr.write("@RELATION " + name + "\n\n");
        wr.write("@ATTRIBUTE Age numeric\n");
        wr.write("@ATTRIBUTE Revisions numeric\n");
        wr.write("@ATTRIBUTE Bugfix numeric\n");
        wr.write("@ATTRIBUTE LOCs numeric\n");
        wr.write("@ATTRIBUTE LOCs_Touched numeric\n");
        wr.write("@ATTRIBUTE LOCs_Added numeric\n");
        wr.write("@ATTRIBUTE Churn numeric\n");
        wr.write("@ATTRIBUTE Avg_Churn numeric\n");
        wr.write("@ATTRIBUTE Authors_Number numeric\n");
        wr.write("@ATTRIBUTE Average_Change_Set numeric\n");
        wr.write("@ATTRIBUTE Buggy {false,true}\n\n");
        wr.write("@DATA\n");
    }

    public static int getMaxReleaseNumber(FileReader fr) {
        try (BufferedReader br = new BufferedReader(fr)) {
            String line;
            String lastLine = null;
            while ((line = br.readLine()) != null) {
                lastLine = line;
            }
            return Integer.parseInt(lastLine.split(",")[0]);
        } catch (IOException e) {
            return 0;
        }
    }


    private static void walkForward(String outputDirectoryPath, String inputFilePath){

        try {
            String outputFilePathTrain;
            String outputFilePathTest;
            String line;

            for(int index = 2; index <= getMaxReleaseNumber(new FileReader(inputFilePath)); index++){
                outputFilePathTrain = outputDirectoryPath + "/" + index + "/" + "Train.arff";
                outputFilePathTest = outputDirectoryPath + "/" + index + "/" + "Test.arff";
                new File(outputDirectoryPath + "/" + index).mkdir();

                FileWriter fileWriterTrain = new FileWriter(outputFilePathTrain);
                arffInit(fileWriterTrain, "Train");
                FileWriter fileWriterTest = new FileWriter(outputFilePathTest);
                arffInit(fileWriterTest, "Test");

                BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));

                while ((line = reader.readLine()) != null) {
                    String[] values = line.split(",");
                    if(!values[0].equals("Version")){
                        if(Integer.parseInt(values[0]) == index){
                            // csv di testing
                            writeArffLine(fileWriterTest, values);
                        } else if (Integer.parseInt(values[0]) < index) {
                            // csv di training
                            writeArffLine(fileWriterTrain, values);
                        } else {
                            break;
                        }
                    }
                }
                reader.close();
                fileWriterTrain.close();
                fileWriterTest.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void startWalkForward(String bookkeeperOutputPath, String openjpaOutputPath) {
        new File(bookkeeperOutputPath).mkdir();
        String inputFilePath = "/Users/pierpaolospaziani/File/Università/ISW2/ISW/BOOKKEEPER.csv";
        walkForward(bookkeeperOutputPath, inputFilePath);

        new File(openjpaOutputPath).mkdir();
        inputFilePath = "/Users/pierpaolospaziani/File/Università/ISW2/ISW/OPENJPA.csv";
        walkForward(openjpaOutputPath, inputFilePath);
    }
}