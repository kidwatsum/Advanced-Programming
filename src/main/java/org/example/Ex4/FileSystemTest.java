package org.example.Ex4;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Partial exam II 2016/2017
 */
class File implements Comparable<File> {
    String name;
    int size;
    LocalDateTime createdAt;

    public File( String name, int size, LocalDateTime createdAt) {
        this.name = name;
        this.size = size;
        this.createdAt = createdAt;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int compareTo(File o) {
        return Comparator.comparing(File::getCreatedAt).thenComparing(File::getName).thenComparing(File::getSize).compare(this,o);
    }

    @Override
    public String toString() {
        return String.format("%-10s %5dB %s",name,size,createdAt);
    }
}
class FileSystem{
    private HashMap<Character,List<File>> files;

    public FileSystem() {
        files=new HashMap<>();
    }
    public void addFile(char folder,String name,int size,LocalDateTime createdAt){
        files.putIfAbsent(folder,new ArrayList<>());
        File f=new File(name, size, createdAt);
        files.get(folder).add(f);
    }
    public List<File> findAllHiddenFilesWithSizeLessThen(int size){
        return files.values().stream().flatMap(List::stream).filter(f->f.getName().startsWith(".")).filter(f->f.getSize()<size).collect(Collectors.toList());

    }
    public int totalSizeOfFilesFromFolders(List<Character> folder){
        //TODO
        int firstSum=0;
        int finalSum=0;
        for(Character c:folder){
            List<File> f=files.get(c);
            if(f!=null){
                firstSum=f.stream().mapToInt(File::getSize).sum();
                finalSum+=firstSum;
            }
        }
        return finalSum;
    }
    private static int getDateFromFile(File file){
        return file.getCreatedAt().getYear();
    }
    private static String getMonthAndDay(File file){
        LocalDateTime date=file.getCreatedAt();

        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("MMMM-dd");
        return date.format(formatter).toUpperCase();

        /*String month= String.valueOf(file.getCreatedAt().getMonth().getDisplayName(Calendar.MONTH,Locale.getDefault()));
        String day= String.valueOf(file.getCreatedAt().getDayOfMonth());
        return month+"-"+day;*/
    }
    private static Set<File> getFiles(HashMap<Character,List<File>> f){
        Set<File> fileSet=new HashSet<>();
        for(List<File> fileList:f.values()){
            fileSet.addAll(fileList);
        }
        return fileSet;
    }
    private static long getSizeOfFolders(List<File> f){
        return f.stream().mapToLong(File::getSize).sum();
    }
    public Map<Integer,Set<File>> byYear(){
        Map<Integer,Set<File>> f=new HashMap<>();
        Set<File> fileSet=getFiles(files);
        for(File fl:fileSet){
            int date=getDateFromFile(fl);
            f.computeIfAbsent(date,k->new HashSet<>()).add(fl);
        }
        return f;

    }
    public Map<String,Long> sizeByMonthAndDay(){
        Map<String,Long> file=new HashMap<>();
        Set<File> fileSEt=getFiles(files);
        for(File f:fileSEt){
            String monthAndDay=getMonthAndDay(f);
            long size=f.getSize();
            file.merge(monthAndDay,size,Long::sum);
        }
        return file;
    }

}

public class FileSystemTest {
    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystem();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            fileSystem.addFile(parts[0].charAt(0), parts[1],
                    Integer.parseInt(parts[2]),
                    LocalDateTime.of(2016, 12, 29, 0, 0, 0).minusDays(Integer.parseInt(parts[3]))
            );
        }
        int action = scanner.nextInt();
        if (action == 0) {
            scanner.nextLine();
            int size = scanner.nextInt();
            System.out.println("== Find all hidden files with size less then " + size);
            List<File> files = fileSystem.findAllHiddenFilesWithSizeLessThen(size);
            files.forEach(System.out::println);
        } else if (action == 1) {
            scanner.nextLine();
            String[] parts = scanner.nextLine().split(":");
            System.out.println("== Total size of files from folders: " + Arrays.toString(parts));
            int totalSize = fileSystem.totalSizeOfFilesFromFolders(Arrays.stream(parts)
                    .map(s -> s.charAt(0))
                    .collect(Collectors.toList()));
            System.out.println(totalSize);
        } else if (action == 2) {
            System.out.println("== Files by year");
            Map<Integer, Set<File>> byYear = fileSystem.byYear();
            byYear.keySet().stream().sorted()
                    .forEach(key -> {
                        System.out.printf("Year: %d\n", key);
                        Set<File> files = byYear.get(key);
                        files.stream()
                                .sorted()
                                .forEach(System.out::println);
                    });
        } else if (action == 3) {
            System.out.println("== Size by month and day");
            Map<String, Long> byMonthAndDay = fileSystem.sizeByMonthAndDay();
            byMonthAndDay.keySet().stream().sorted()
                    .forEach(key -> System.out.printf("%s -> %d\n", key, byMonthAndDay.get(key)));
        }
        scanner.close();
    }
}

// Your code here


