import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Linked implements Allocation {
    public Linked() {
    }
    @Override
    public boolean allocate(String dirName, int level, File file) {
        // TODO Auto-generated method stub

        int size = file.getLength();
        int max = FileManager.workDisk.getNumberOfBlocks();

        // search for an array of blocks that are free at different places
        ArrayList<Integer> freeBlocks = new ArrayList<Integer>();
        while(true){
            int index = (int)Math.floor(Math.random()*(max +1)+0);
            if (FileManager.workDisk.getBlocks()[index] == -1) {
                freeBlocks.add(index);
                FileManager.workDisk.getBlocks()[index] = file.getId();
            }
            // if we found the exact number of blocks
            if(freeBlocks.size() == size )
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

        writeFile(file);
          return  true;
    }



    public void writeFile(File file) {
        FileWriter fw = null; BufferedWriter bw = null; PrintWriter pw = null;
        // open allocation file for appending
        try {
            fw = new FileWriter("allocation.txt", true);
            bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // write file on the form path start lock end block
        // start block   next block on the list
        // another block next block on the list
        // ....
        // end block    nil -> point to nothing
        String fileInfo = "";
        fileInfo += file.getPath();
        fileInfo += " ";
        fileInfo += String.valueOf(file.getStartBlock());
        fileInfo += " ";
        fileInfo += String.valueOf(file.getEndBlock());
        pw.println(fileInfo);

            int block1 = 0,block2 = 0;
            int n = file.getAllocatedBlocks().size();
            for(int i = 0; i < n; i++)
            {
                block1 = file.getAllocatedBlocks().get(i);
                if(i + 1 < n)
                {
                    block2 = file.getAllocatedBlocks().get(i+1);
                }
                fileInfo = "";
                fileInfo += String.valueOf(block1);
                fileInfo += " ";
                if(i+1 < n)
                {
                    fileInfo += String.valueOf(block2);
                }
                else {
                    fileInfo += "nil";
                }
                pw.println(fileInfo);
            }
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