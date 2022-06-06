import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Disk {
    private final int diskSize;
    private final int numberOfBlocks;
    private int numberOfDirectories;
    private ArrayList<Directory> directories; // list of all directories that are on the system help for searching
    private Directory root; // default root directory

    private int[] Blocks; // an array of block in the disk

    public Disk(int numberOfBlocks) throws FileNotFoundException {
        this.numberOfBlocks = numberOfBlocks;
        numberOfDirectories = 0;
        diskSize = numberOfBlocks; // 1 block = 1 KB
        directories = new ArrayList<Directory>();
        Blocks = new int[numberOfBlocks];
        for (int i = 0; i < numberOfBlocks; i++) {
            Blocks[i] = -1;
        }
        root = new Directory("root", 0); // create default directory
        addDiskDirectory(root);// add default root directory
    }

    public int getNumberOfBlocks() {
        return numberOfBlocks;
    }



    public int getNumberOfDirectories() {
        return numberOfDirectories;
    }

    public void addDiskDirectory(Directory directory) {
        directories.add(directory);
        numberOfDirectories++;
    }


    // make a block belong to specific file
    public void allocateBlock(int blockNumber, int FileId) {
        Blocks[blockNumber] = FileId;

    }
    // free block
    public void freeBlock(int blockNumber) {
        Blocks[blockNumber] = -1;
    }

    // allocate a list of blocks that belong to specific file
    public void allocateBlocks(int FileId, ArrayList<Integer> blocks) {
        for (int i = 0; i < blocks.size(); i++) {
            allocateBlock(blocks.get(i), FileId);
        }
    }

    // free list of blocks that belong to specific file
    public void deAllocatedBlocks(ArrayList<Integer> blocks) {
        for (int i = 0; i < blocks.size(); i++) {
            freeBlock(blocks.get(i));
        }

    }

    // get available space on the disk
    public int getEmptySpace() {
        int emptySpace = 0;
        for (int i = 0; i < numberOfBlocks; i++) {
            if (Blocks[i] == -1) {
                emptySpace++;
            }
        }
        return emptySpace;
    }
    // get allocated space on the disk
    public int getAllocatedSpace() {
        return diskSize - getEmptySpace();
    }

    public void printBlocks() {
        for (int i = 0; i < numberOfBlocks; i++) {
            if (Blocks[i] != -1) {
                System.out.println("Block " + i + " is allocated by file " + Blocks[i]);
            } else {
                System.out.println("Block " + i + " is free");
            }
        }
    }

    // if we delete directory we remove it from directories list that contain all directories on the disk
    public void removeFromDirectories(String name,int level)
    {
        int n = getNumberOfDirectories();
        for (int i = 0; i < n; ++i) {
            String dirName = directories.get(i).getName();
            int dirLevel = directories.get(i).getLevel();
            if (dirName.equals(name) && dirLevel == level) {
                directories.remove(i);
                numberOfDirectories--;
                break;
            }
        }
    }

    public int[] getBlocks() {
        return Blocks;
    }

    // search if specific directory at some level is existed
    public boolean searchDirectories(String name, int level) {
        int n = getNumberOfDirectories();
        for (int i = 0; i < n; ++i) {
            String dirName = directories.get(i).getName();
            int dirLevel = directories.get(i).getLevel();
            if (dirName.equals(name) && dirLevel == level) {
                return true;
            }
        }
        return false;
    }

    // get a subdirectory that it's exist a specific level on the disk
    public Directory getDirectory(String name, int level) {
        Directory target = root.getDir(name, level);
        if(  target == null)
        {
            return  root;
        }
        return  target;

    }

    public Directory getRoot()
    {
        return root;
    }

}