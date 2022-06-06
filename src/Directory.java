import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Directory {
    private String name;
    private int level;
    private ArrayList<File> files; // list of files that belong to this directory
    private ArrayList<Directory> subDirectories; // list of subdirectories that belong to that directory

    public Directory(String name, int level) {
        this.name = name;
        this.level = level;
        this.files = new ArrayList<File>();
        this.subDirectories = new ArrayList<Directory>();
    }
    // add file to list of files
    public void addFile(File file) {
        files.add(file);
        // update vsf file text after adding file to the system
        try {
            updateVSF();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // ren=move file from list fo files
    public void removeFile(String fileName)
    {
        for(int i = 0; i < files.size(); ++i)
        {
            if(files.get(i).getName().equals(fileName))
            {
                // first deallocate blocks that is allocated to this file
                FileManager.workDisk.deAllocatedBlocks(files.get(i).getAllocatedBlocks());

                // remove file
                files.remove(i);
                break;
            }
        }
        // update vsf file text after deleting file from the system
        try {
            updateVSF();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    // add directory to list of subdirectories
    public void addDirectory(Directory directory) {
        subDirectories.add(directory);
        // update vsf file text after adding directory to the system
        try {
            updateVSF();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // remove all the files that belong to that directory
    public void removeMyFiles()
    {
        int i = 0;
        while(files.size() != 0)
        {
           String fileName =  files.get(i).getName();
           // remove file and deallocate it's blocks
            removeFile(fileName);
            i++;
        }
    }
    // remove directory from list of subdirectories
    public void removeDirectory(String directoryName) {

        for(int i = 0; i < subDirectories.size(); ++i)
        {
            if(subDirectories.get(i).getName().equals(directoryName))
            {
                // if directory contain files first we remove them
                subDirectories.get(i).removeMyFiles();
                // second remove the directory
                subDirectories.remove(i);
                break;
            }
        }
        // update vsf file after removing directory from the system
        try {
            updateVSF();
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // get level of directory
    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    // search if directory contain a specific file
    public boolean containFile(String name) {
        int n = files.size();
        for (int i = 0; i < n; ++i) {
            if (files.get(i).getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    // check if directory contain a specific directory
    public boolean containDirectory(String name) {
        int n = subDirectories.size();
        for (int i = 0; i < n; ++i) {
            if (subDirectories.get(i).getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    // get directory a specific directory form list of subdirectories
    public Directory getDir(String name, int level)
    {
        Directory dir = null;
        for(int i = 0; i < subDirectories.size(); ++i)
        {
            // if we found directory
            if(subDirectories.get(i).getName().equals(name) && subDirectories.get(i).getLevel() == level)
            {
                 dir = subDirectories.get(i);
                break;
            }
            // recursive search into other directories
            else {
                return subDirectories.get(i).getDir(name,level);
            }
        }
        return dir;
    }

    public String createLevel(int level)
    {
        String l = "";
        for(int i = 0; i < level; ++i)
        {
            l += "  ";
        }
        return l;
    }
    // print the system structure
    public void printDirectoryStructure(int level)
    {
        FileWriter fw = null; BufferedWriter bw = null; PrintWriter pw = null;

        try {
            fw = new FileWriter("vsf.txt", true);
            bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        String spaces = createLevel(level);
        String Dirname = spaces+"<" + name + ">";
        pw.println(Dirname);

        for(int i = 0; i < files.size(); ++i)
        {
            String fileName = spaces+" "+getFiles().get(i).getName();
            pw.println(fileName);
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
        // recursive print each subdirectory
        for(int i = 0; i < subDirectories.size(); ++i)
        {
            int myLevel = subDirectories.get(i).getLevel();
            subDirectories.get(i).printDirectoryStructure(myLevel);
        }

    }

    // update vsf file text after adding or deleting file/directory
    public void updateVSF() throws IOException
    {
        FileWriter fwOb = new FileWriter("vsf.txt", false);
        PrintWriter pwOb = new PrintWriter(fwOb, false);
        pwOb.flush();
        pwOb.close();
        fwOb.close();
        FileManager.workDisk.getRoot().printDirectoryStructure(0);

    }

    public ArrayList<File> getFiles()
    {
        return files;
    }
}