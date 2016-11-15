package edu.gdut.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import jxl.Cell;
import jxl.CellType;
import jxl.NumberCell;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * Author:  rainj2013
 * Email:  yangyujian25@gmail.com
 * Date:  16-11-13 下午9:46
 */
public class XlsUtil {

    private static List<Double[]> read(InputStream ins, int sheetIndex) throws IOException, BiffException {
        // 创建一个list 用来存储读取的内容
        List<Double[]> list = new ArrayList<>();
        Workbook rwb;
        Cell cell = null;
        NumberCell nCell;
        // 创建输入流
        InputStream stream = ins;
        // 获取Excel文件对象
        rwb = Workbook.getWorkbook(stream);
        // 获取文件的指定工作表(sheet)，默认第一个
        jxl.Sheet sheet = rwb.getSheet(sheetIndex);
        // 行数(表头(下标0)不需要，从1开始)
        for (int i = 1; i < sheet.getRows(); i++) {
            // 创建一个数组 用来存储每一行的值
            Double[] row = new Double[sheet.getColumns()];
            // 列数
            for (int j = 0; j < sheet.getColumns(); j++) {
                // 获取第i行，第j列的值
                cell = sheet.getCell(j, i);
                if(cell.getType()==CellType.NUMBER){
                    nCell = (NumberCell) cell;
                    row[j] = nCell.getValue();
                }
            }
            //空白的就不要了吧
            if (cell.getType()==CellType.EMPTY){
                continue;
            }
            // 把刚获取的列存入list
            list.add(row);
        }
        return list;
    }

    /**
     * @Description xls文件解析读取训练集/测试集数据
     * @param ins   输入流
     * @param sheetIndex sheet编号
     * @param featureSize feature个数
     * @return  xls信息
     * @throws BiffException
     * @throws IOException
     */
    public static HashMap<String,List<Double[]>> readData(InputStream ins, int sheetIndex, int featureSize) throws BiffException, IOException {
        HashMap<String,List<Double[]>> result = new HashMap<>();
        List<Double[]> list = read(ins, sheetIndex);

        for(int objectId = 1;objectId<=list.size()/featureSize;objectId++){
            List<Double[]> object = new ArrayList<>();
            for (int rowNum = (objectId-1)*7; rowNum<(objectId-1)*7+featureSize;rowNum++){
                Double[] row = list.get(rowNum);
                object.add(new Double[]{row[2],row[3],row[4]});
            }
            result.put(Integer.toString(objectId),object);
        }

        return result;
    }

    public static HashMap<String,List<Double[]>> readData(String realPath, int sheetIndex, int featureSize) throws BiffException, IOException {
        InputStream ins = new FileInputStream(realPath);
        return readData(ins, sheetIndex, featureSize);
    }

    /**
     * @Description xls文件解析读取标签数据
     * @param ins   输入流
     * @param sheetIndex sheet编号
     * @return  xls信息
     * @throws BiffException
     * @throws IOException
     */
    public static List<Integer> readLabel(InputStream ins, int sheetIndex) throws IOException, BiffException {
        List<Integer> fraudLabels = new ArrayList<>();
        List<Double[]> list = read(ins, sheetIndex);
        for (int objectId = 1;objectId<=list.size();objectId++){
            Double[] object = list.get(objectId-1);
            fraudLabels.add((int)object[1].doubleValue());
        }
        return fraudLabels;
    }

    public static List<Integer> readLabel(String realPath, int sheetIndex) throws IOException, BiffException {
        InputStream ins = new FileInputStream(realPath);
        return readLabel(ins, sheetIndex);
    }

    public static void writeXls(OutputStream out, Map<String, Double[]> data){
        //TODO
    }

}

