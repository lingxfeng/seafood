package com.eastinno.otransos.application.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * @version 2.0 图片水印处理
 * @author lengyu
 * @date 2014年7月26日-下午7:01:59
 */
public final class ImageUtils {
    public ImageUtils() {

    }

    /**
     * public final static String getPressImgPath() { return ApplicationContext
     * .getRealPath("/template/data/util/shuiyin.gif"); }
     */

    /**
     * 把图片印刷到图片上
     * 
     * @param pressImg -- 水印文件
     * @param targetImg -- 目标文件
     * @param x --x坐标
     * @param y --y坐标
     */
    public final static void pressImage(String pressImg, String targetImg, int x, int y, float f) {
        try {
            // 目标文件
            File _file = new File(targetImg);
            Image src = ImageIO.read(_file);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();
            g2d.drawImage(src, 0, 0, wideth, height, null);

            // 水印文件
            File _filebiao = new File(pressImg);
            Image src_biao = ImageIO.read(_filebiao);
            int wideth_biao = src_biao.getWidth(null);
            int height_biao = src_biao.getHeight(null);
            AlphaComposite ac = setAlpha(f);// 设置水印字体透明度
            g2d.setComposite(ac);
            g2d.drawImage(src_biao, (wideth - wideth_biao) / 2, (height - height_biao) / 2, wideth_biao, height_biao, null);
            // 水印文件结束
            g2d.dispose();
            FileOutputStream out = new FileOutputStream(targetImg);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(image);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static AlphaComposite setAlpha(float f) {
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, f);// 设置水印字体透明度
        return ac;
    }

    /**
     * 打印文字水印图片
     * 
     * @param pressText --文字
     * @param targetImg -- 目标图片
     * @param fontName -- 字体名
     * @param fontStyle -- 字体样式
     * @param color -- 字体颜色
     * @param fontSize -- 字体大小
     * @param x -- 偏移量
     * @param y
     */
    public static void pressText(String pressText, String targetImg, String fontName, int fontStyle, int color, int fontSize, int x, int y,
            float f) {
        try {
            File _file = new File(targetImg);
            Image src = ImageIO.read(_file);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();
            g2d.drawImage(src, 0, 0, wideth, height, null);

            g2d.setColor(Color.blue);
            g2d.setFont(new Font(fontName, fontStyle, fontSize));
            AlphaComposite ac = setAlpha(f);// 设置水印字体透明度
            g2d.setComposite(ac);
            g2d.drawString(pressText, wideth - fontSize - x, height - fontSize / 2 - y);
            g2d.dispose();
            FileOutputStream out = new FileOutputStream(targetImg);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(image);
            out.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * 图像切割(按指定起点坐标和宽高切割) by wangyin
     * 
     * @param srcImageFile 源图像地址
     * @param result 切片后的图像地址
     * @param x 目标切片起点坐标X
     * @param y 目标切片起点坐标Y
     * @param width 目标切片宽度
     * @param height 目标切片高度
     */
    public static void cut(String srcImageFile, String result, int x, int y, int width, int height) {
        try {
            // 读取源图像
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            int srcWidth = bi.getHeight(); // 源图宽度
            int srcHeight = bi.getWidth(); // 源图高度
            if (srcWidth > 0 && srcHeight > 0) {
                Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
                ImageFilter cropFilter = new CropImageFilter(x, y, width, height);
                Image img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
                BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics g = tag.getGraphics();
                g.drawImage(img, 0, 0, width, height, null); // 绘制切割后的图
                g.dispose();
                ImageIO.write(tag, "JPEG", new File(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        pressText("梦坊国际", "D:\\333.jpg", "微软雅黑", 10, 15, 40, 400, 400, 0.3f);
        pressImage("d:\\login.jpg", "D:\\333.jpg", 260, 260, 0.3f);
    }
}
