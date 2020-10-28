
/**
 * @author dell
 * @date 2020/10/12
 * @copyright© 2020
 */
public class Kurios {
    /**
     * 峭度因子
     */
    public static float getKurios(float[] data){
        float a = 0;
        float mean = 0;
        float b = 0;
        float var = 0;
        for (float datum : data){
            a += Math.pow(datum,4);
            b += datum;
        }
        mean = b / data.length;
        for (float x : data){
            var += (float) Math.pow((x - mean), 2) / (data.length-1);
        }
        float f = a / data.length;
        float c = (float) Math.pow(f, 0.25);
        float d = (float) Math.pow(var, 0.5);
        return c / d;
    }

    public static void main(String[] args) {

    }
}
