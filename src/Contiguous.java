import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Contiguous implements Allocation {
    public Contiguous() {}

    @Override
    public boolean allocate(String dirName, int level, File file) {
        // allocate contiguous memory
        int size = file.getLength();

        // search for a contiguous blocks
        for (int i = 0; i < FileManager.workDisk.getNumberOfBlocks(); i++) {
            if (FileManager.workDisk.getBlocks()[i] == -1) {
                int j = i;
                while (j < FileManager.workDisk.getNumberOfBlocks() && FileManager.workDisk.getBlocks()[j] == -1) {
                    j++;

                    // if we found a contiguous blocks without any gap we create file
                    if (j - i == size) {

                        // start adding file info
                        file.setStartBlock(i);
                        file.setEndBlock(i + size - 1);
                        file.setAllocatedBlocks(new ArrayList<Integer>());
                        for (int k = i; k < i + size; k++) {
                            file.getAllocatedBlocks().add(k);
                        }
                        // add file to it's directory
                        FileManager.workDisk.getDirectory(dirName,level).addFile(file);

                        // update allocated blocks
                        FileManager.workDisk.allocateBlocks(file.getId(),file.getAllocatedBlocks());

                        // write file to allocation.txt
                        try {
                            writeFile(file);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        return true;
                    }
                }

            }
        }

        return false;
    }

    // write file 
    public void writeFile(File file) throws IOException {
        FileWriter fw = null; BufferedWriter bw = null; PrintWriter pw = null;
        // open file for appending
        try {
            fw = new FileWriter("allocation.txt", true);
            bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // write file in the form filepath start block end block
        String fileInfo = "";
        fileInfo += file.getPath();
        fileInfo += " ";
        fileInfo += String.valueOf(file.getStartBlock());
        fileInfo += " ";
        fileInfo += String.valueOf(file.getLength());

       pw.println(fileInfo);
       pw.flush();
        try {
            fw.close();
            bw.close();
            pw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}