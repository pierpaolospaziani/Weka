import control.RetrieveWekaInfo;
import files.EvaluationFile;
import model.AllEvaluationLists;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class ClassifierClass {

    public static void main(String[] args) throws Exception {
        String bookkeeperPath = "BookkeeperWalkForward";
        String openjpaPath = "OpenjpaWalkForward";

        WalkForward.startWalkForward(bookkeeperPath,openjpaPath);

        String projName = "bookkeeper";
        RetrieveWekaInfo retWekaInfo = new RetrieveWekaInfo(bookkeeperPath+"/", 7);
        AllEvaluationLists allLists = retWekaInfo.retrieveClassifiersEvaluation(projName);

        EvaluationFile evaluationFileAvg = new EvaluationFile(projName, allLists.getAvgEvaluationsList(), "avg");
        evaluationFileAvg.csvWrite(projName);
        EvaluationFile evaluationFileDetails = new EvaluationFile(projName, allLists.getMergeEvaluationsList(), "details");
        evaluationFileDetails.csvWrite(projName);

        projName = "openjpa";
        retWekaInfo = new RetrieveWekaInfo(openjpaPath+"/", 15);
        allLists = retWekaInfo.retrieveClassifiersEvaluation(projName);

        evaluationFileAvg = new EvaluationFile(projName, allLists.getAvgEvaluationsList(), "avg");
        evaluationFileAvg.csvWrite(projName);
        evaluationFileDetails = new EvaluationFile(projName, allLists.getMergeEvaluationsList(), "details");
        evaluationFileDetails.csvWrite(projName);

        cleanUp(bookkeeperPath);
        cleanUp(openjpaPath);
    }
 
    public static void cleanUp(String path) {
        try {
            Files.walkFileTree(Paths.get(path), new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (Exception ignored){}
    }
}
