import java.util.ArrayList;

public class File {
    private String name;
    private int length;
    private int id;
    private int startBlock;
    private int endBlock;
    private String path;
    private ArrayList<Integer> allocatedBlocks;

    public File(String name, int length, int id, String path) {
        this.name = name;
        this.length = length;
        this.id = id;
        this.path = path;
        allocatedBlocks = new ArrayList<Integer>();
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }

    public void setAllocatedBlocks(ArrayList<Integer> allocatedBlocks) {
        this.allocatedBlocks = allocatedBlocks;
    }

    public ArrayList<Integer> getAllocatedBlocks() {
        return allocatedBlocks;
    }

    public int getId() {
        return id;
    }

    public void setStartBlock(int startBlock) {
        this.startBlock = startBlock;
    }

    public int getStartBlock() {
        return startBlock;
    }

    public void setEndBlock(int endBlock) {
        this.endBlock = endBlock;
    }

    public int getEndBlock() {
        return endBlock;
    }

    public String getPath() {
        return path;
    }
}