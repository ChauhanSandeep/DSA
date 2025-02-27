package Hashing;
import java.util.*;

/**
 * Given list of directory path, file name and content. Group the files with duplicate content together.
 */
public class DuplicateFile {
    public static void main(String[] args) {
        String[] paths = {
                "root/a 1.txt(abcd) 2.txt(efgh)",   // 1.txt and 2.txt in location root/a
                "root/c 3.txt(abcd)",               // 3.txt in location root/c
                "root/c/d 4.txt(efgh)",             // 4.txt in location root/c/d
                "root 4.txt(efgh)"};                // 4.txt in location root
        List<List<String>> duplicates = new DuplicateFile().findDuplicate(paths);
        System.out.println(duplicates);
    }
    public List<List<String>> findDuplicate(String[] paths) {
//         {"root/a 1.txt(abcd) 2.txt(efgh)","root/c 3.txt(abcd)","root/c/d 4.txt(efgh)","root 4.txt(efgh)"}
        List<List<String>> result = new ArrayList<>();
        if(paths == null || paths.length == 0) return result;

        Map<String, List<String>> contentPathMap = new HashMap<>();

        for(String path: paths) {
            String[] pathSplitArr = path.split("\\s+");

            for(int i=1; i<pathSplitArr.length; i++) {
                int index = pathSplitArr[i].indexOf("(");
                String fileContent = pathSplitArr[i].substring(index);
                String filePath = pathSplitArr[0] + "/" + pathSplitArr[i].substring(0, index);
                List<String> set = contentPathMap.getOrDefault(fileContent, new ArrayList<>());
                set.add(filePath);
                contentPathMap.put(fileContent, set);
            }

        }

        for(Map.Entry<String, List<String>> entry: contentPathMap.entrySet()) {
            if(entry.getValue().size() > 1){
                result.add(new ArrayList<>(entry.getValue()));
            }
        }
        return result;

    }
}
