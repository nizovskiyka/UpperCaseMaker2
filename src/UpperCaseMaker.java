import java.lang.annotation.*;
import java.lang.reflect.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
//to upper case annotation
@interface ToUpper
{
}

class TestClass{
    @ToUpper
    private String x;
    public String getX() {
        return x;
    }
    public void setX(String x) {
        this.x = x;
    }
}

public class UpperCaseMaker {

    public static void main(String[] args) {

        //creating a logger
        Logger logger = LoggerFactory.getLogger(UpperCaseMaker.class);

        TestClass tClass = new TestClass();

        tClass.setX("abcd");

        //System.out.println("x = " + tClass.getX());

        //accessing metadata about UpperCaseMaker.class
        Class<?> cl = tClass.getClass();

        //getting fields inside UpperCaseMaker.class
        Field[] fields  = cl.getDeclaredFields();

        for(Field f: fields)
        {

            //getting all annotations
            Annotation[] fDeclaredAnnotations = f.getDeclaredAnnotations();

            for(Annotation anno : fDeclaredAnnotations)
            {
                //if the field annotated by ToUpper
                if(anno.annotationType().equals(ToUpper.class))
                {

                    //private? really, don't care
                    f.setAccessible(true);
                    try {
                        //changing field value
                        f.set(tClass, ((String)f.get(tClass)).toUpperCase());
                        logger.warn("The variable " + f.getName() + " has been forced to upper case");

                    } catch (IllegalAccessException e) {

                        System.out.println("well, fuck it");
                        logger.warn("The variable " + f.getName() + " has failed to be forced to upper case");
                        e.printStackTrace();
                    }
                }
            }
        }
        System.out.println("x = "  + tClass.getX());
    }
}