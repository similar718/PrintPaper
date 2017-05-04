package getimageandservice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/1.
 */
public class Person {

    public List<String> getFilePaths() {
        return FilePaths;
    }

    public void setFilePaths( List<String> filePaths) {
        FilePaths = filePaths;
    }

    private List<String> FilePaths;
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<ImageEntity> getImgLst() {
        return ImgLst;
    }

    public void setImgLst(List<ImageEntity> imgLst) {
        ImgLst = imgLst;
    }

    private String ImgFolder;
    /**
     * 具体天气的文字说明
     */
    private String fileName;
    public List<ImageEntity> ImgLst=new ArrayList<ImageEntity>();

    public String getImgFolder() {
        return ImgFolder;
    }

    public void setImgFolder(String imgPath) {
        ImgFolder = imgPath;
    }

}
