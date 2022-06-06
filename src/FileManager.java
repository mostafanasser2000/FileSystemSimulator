import java.io.*;
import java.util.Scanner;

public class FileManager {
    public  static Disk workDisk;
    public static int id; // and id for every file
    public static int choice;
    private final Allocation contiguousAllocationTechnique;
    private final Allocation linkedAllocationTechnique;
    private final Allocation indexedAllocationTechnique;
    public static Scanner input;
    boolean validPath;

    public FileManager(Disk disk) throws IOException {
        workDisk = disk;
        id = 1;
        contiguousAllocationTechnique = new Contiguous();
        linkedAllocationTechnique = new Linked();
        indexedAllocationTechnique = new Indexed();

        // clear the content of vsf file
        FileWriter fwOb = new FileWriter("vsf.txt", false);
        PrintWriter pwOb = new PrintWriter(fwOb, false);
        pwOb.flush();
        pwOb.close();
        fwOb.close();
        // clear the content of allocation file
        fwOb = new FileWriter("allocation.txt", false);
        pwOb = new PrintWriter(fwOb, false);
        pwOb.flush();
        pwOb.close();
        fwOb.close();

        input = new Scanner(System.in);
    }

    // check if  path is valid or not
    boolean checkValidPath(String Path) {
        String[] parts = Path.split("/"); // divide path based on "/' delimiter
        int len = parts.length;

        // check if every directory on the path exist or not
        for (int i = 0; i < len - 1; i++) {
            String targetDirectoryName = parts[i];
            boolean found = workDisk.searchDirectories(targetDirectoryName, i);
            // if one of the directories is not found then it's not a valid path
            if (!found) {
                return false;
            }
        }
        return true;
    }

    // function to create a file
    public void createFile(String path, int size) {
        // first stage: check if it's a valid path
        if (!checkValidPath(path)) {
            System.out.println("Invalid Path");
            return ;
        }

        String[] parts = path.split("/"); // divide path based on "/' delimiter
        int len = parts.length;
        String fileName = parts[len - 1]; // get file name
        String targetDirectoryName = parts[len - 2]; // directory that will contain that file
        int targetLevel = len - 2; // level of that directory

        /* second stage: get the directory that will hold file and check if the file
         already exist on it */
        if (workDisk.getDirectory(targetDirectoryName, targetLevel).containFile(fileName)) {
            System.out.println("File already exist");
            return ;
        }

        // third stage: check if there is enough space for file
        if(workDisk.getEmptySpace() < size )
        {
            System.out.println("There is no enough space already exist");
            return ;
        }
        File createdFile = new File(fileName,size,id,path);
        // check allocation technique
        System.out.println("Please choose allocation Method");
        boolean sucessAllocation = false;

        while(true)
        {
            System.out.println("Enter 1 for Contiguous Allocation");
            System.out.println("Enter 2 for Linked Allocation");
            System.out.println("Enter 3 for Indexed Allocation");
            choice = input.nextInt();
            if(choice == 1)
            {
                sucessAllocation = contiguousAllocationTechnique.allocate(targetDirectoryName, targetLevel, createdFile);
            }
            else if(choice == 2)
            {
                sucessAllocation = linkedAllocationTechnique.allocate(targetDirectoryName, targetLevel, createdFile);
            }
            else if(choice == 3)
            {
                sucessAllocation = indexedAllocationTechnique.allocate(targetDirectoryName, targetLevel, createdFile);
            }

            if(sucessAllocation)
            {
                System.out.println("file is created successfully");
                id++;
                break;
            }
            else {
                System.out.println("Invalid choice or can't allocate using this technique");
            }

        }

    }

    // function to delete file
    public void deleteFile(String path) {
        // first stage: check if it's a valid path
        if (!checkValidPath(path)) {
            System.out.println("Invalid Path");
            return ;
        }

        String[] parts = path.split("/");
        int len = parts.length;
        String fileName = parts[len - 1]; // get file name
        String targetDirectoryName = parts[len - 2]; // directory that  contain that file
        int targetLevel = len - 2; // level of target directory

        // check if there is no file with this name in the directory
        if (!(workDisk.getDirectory(targetDirectoryName, targetLevel).containFile(fileName))) {
            System.out.println("File is not exist ");
            return ;
        }
        else
        {
            // remove file from directory
            workDisk.getDirectory(targetDirectoryName, targetLevel).removeFile(fileName);
        }

    }

    // delete Directory
    public void deleteDirectory(String path) {
        // first stage: check if it's a valid path
        if (!checkValidPath(path)) {
            System.out.println("Invalid Path");
            return ;
        }

        String[] parts = path.split("/");
        int len = parts.length;
        String directoryName = parts[len - 1]; // get directory name
        String targetDirectoryName = parts[len - 2]; // directory that contain that directory
        int targetLevel = len - 2; // level of target directory

        /* check if there is directory with this name in the target directory*/
        if (!(workDisk.getDirectory(targetDirectoryName, targetLevel).containDirectory(directoryName))) {
            System.out.println("Directory is not exist ");
            return ;
        }
        else
        {   // remove directory from target directory
            workDisk.getDirectory(targetDirectoryName, targetLevel).removeDirectory(directoryName);
        }

    }


    // create Directory
    public void  createDirectory(String path) {
          // first stage: check if it's a valid path
        if (!checkValidPath(path)) {
            System.out.println("Invalid Path");
            return ;
        }

        String[] parts = path.split("/");
        int len = parts.length;
        String directoryName = parts[len - 1]; // get directory name
        String targetDirectoryName = parts[len - 2]; // target directory that will contain created directory
        int targetLevel = len - 2;

        /*check if there is existed a directory with this name in this directory subdirectories */
        if (workDisk.getDirectory(targetDirectoryName, targetLevel).containDirectory(directoryName)) {
            System.out.println("Directory is already exist ");
            return ;
        }

        else
        {  // create directory
            Directory dir = new Directory(directoryName, targetLevel+1);
            // add it to it's parent directory
            workDisk.getDirectory(targetDirectoryName, targetLevel).addDirectory(dir);
            // add directory to list that contain all directories in that system
            workDisk.addDiskDirectory(dir);
        }
    }

    public void printfVSF()
    {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("vsf.txt"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String line;
        try {
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void displayDiskStatus() {
        System.out.println("Disk Status");
        System.out.println("Empty space: " + workDisk.getEmptySpace());
        System.out.println("Allocated space: " + workDisk.getAllocatedSpace());
        workDisk.printBlocks();
    }


}