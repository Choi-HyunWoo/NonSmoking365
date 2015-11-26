package aftercoffee.org.nonsmoking365.utilities;

import android.graphics.Bitmap;

/**
 * Created by Tacademy on 2015-11-27.
 */
public class Utilities {
    public static Bitmap getCenterCroppedBitmap(Bitmap originalBitmap){
        Bitmap dstBmp;
        if (originalBitmap.getWidth() >= originalBitmap.getHeight()){

            dstBmp = Bitmap.createBitmap(
                    originalBitmap,
                    originalBitmap.getWidth()/2 - originalBitmap.getHeight()/2,
                    0,
                    originalBitmap.getHeight(),
                    originalBitmap.getHeight()
            );

        }else{

            dstBmp = Bitmap.createBitmap(
                    originalBitmap,
                    0,
                    originalBitmap.getHeight()/2 - originalBitmap.getWidth()/2,
                    originalBitmap.getWidth(),
                    originalBitmap.getWidth()
            );
        }
        return  dstBmp;
    }
}
