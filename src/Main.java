import java.io.IOException;
import java.util.Scanner;

/**
 * @author Mostafa Nasser Mohamed
 */
public class Main {
    public static void main(String[] args) throws IOException {
        String command ;
        Disk myDisk = new Disk(100);
        FileManager myManger = new FileManager(myDisk);

        while(true)
        {
            System.out.println("Please enter one of these commands");
            System.out.println("1- CreateFile path/fileName size");
            System.out.println("2- CreateFolder path/folderName");
            System.out.println("3- DeleteFile path/filename");
            System.out.println("4- DeleteFolder path/folderName");
            System.out.println("5- DisplayDiskStatus");
            System.out.println("6- DisplayDiskStructure");
            System.out.println("7- exit");

            System.out.print("Command: ");
            Scanner input = new Scanner(System.in);
            command = input.nextLine();
            if(command.equals("exit"))
            {
                System.exit(0);
            }

            System.out.println();
            String[] parts = command.split(" ");
            command = parts[0];
            command =  command.toLowerCase();

            switch (command) {
                case "createfile" -> {
                    String path = parts[1];
                    int size = Integer.parseInt(parts[2]);
                    myManger.createFile(path, size);
                }
                case "createfolder" -> {
                    String path = parts[1];
                    myManger.createDirectory(path);
                }
                case "deletefile" -> {
                    String path = parts[1];
                    myManger.deleteFile(path);

                }
                case "deletefolder" -> {
                    String path = parts[1];
                    myManger.deleteDirectory(path);
                }
                case "displaydiskstatus" -> {
                    myManger.displayDiskStatus();
                }
                case "displaydiskstructure" -> {
                    myManger.printfVSF();
                }
                default -> {
                    System.out.println("Invalid Command");
                }
            }

        }
    }

}