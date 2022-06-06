import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Indexed implements Allocation {
    public Indexed() {}
    @Override
    public boolean allocate(String dirName, int level, File file) {
        int size = file.getLength();
        int freeSpace = FileManager.workDisk.getEmptySpace();
        int max = FileManager.workDisk.getNumberOfBlocks();
        // if empty space is less than size + 1 of the file
        // 1 represent on extra block for index
        if (freeSpace  < size+1 ) {
            return false;
        }

        ArrayList<Integer> freeBlocks = new ArrayList<Integer>();
        // search for an array of blocks that are free at different places
        while(true){
            int index = (int)Math.floor(Math.random()*(max)+0);
            if (FileManager.workDisk.getBlocks()[index] == -1) {
                freeBlocks.add(index);
                FileManager.workDisk.getBlocks()[index] = file.getId();
            }
            // if we found the exact number of blocks
            if(freeBlocks.size() == size + 1 )
            {
                break;
            }
        }
        file.setStartBlock(freeBlocks.get(0));
        file.setEndBlock(freeBlocks.get(size - 1));
        file.setAllocatedBlocks(freeBlocks);

        // add file to it's directory
        FileManager.workDisk.getDirectory(dirName,level).addFile(file);


        // update allocated blocks
        FileManager.workDisk.allocateBlocks(file.getId(),file.getAllocatedBlocks());

        // write file into allocation text file
        writeFile(file);
        return  true;
    }



    public void writeFile(File file) {
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

        // write file on the form path index
        // index: allocated blocks
        String fileInfo = "";
        fileInfo += file.getPath();
        fileInfo += " ";
        fileInfo += String.valueOf(file.getStartBlock());
        pw.println(fileInfo);
            fileInfo = " ";
            fileInfo += String.valueOf(file.getStartBlock());
            fileInfo += ": ";
            for(int i = 1, n = file.getAllocatedBlocks().size(); i < n;++ i)
            {
                fileInfo += String.valueOf(file.getAllocatedBlocks().get(i));
                fileInfo += " ";
            }
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