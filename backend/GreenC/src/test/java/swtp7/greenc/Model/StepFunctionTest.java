package swtp7.greenc.Model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StepFunctionTest extends ModelTestBaseSoftwareSystem2 {

    /**
     * Test method for the checkValidity Method. MinValue is set to 2 and MaxValue to 100000 for all the different StepFunctions
     */
    @Test
    public void checkValidityTest(){

        SoftwareSystem softwareSystem = super.getSoftwareSystem2();
        List<StepFunction> stepFunctionList = new ArrayList<>();
        for(NumericOption numericOption: softwareSystem.getNumericOptions()){
            stepFunctionList.add(numericOption.getStepFunction());
        }
        int count = 0;

        for(StepFunction stepFunction: stepFunctionList){
            if(stepFunction.getName().equals("x + 2")){
                count ++;
                int[] valid = new int[] {2,4,6,8,100,200,1000,3000,50000, 50002};
                int[] invalid = new int[] {5,9,63,81,105,201,1001,3099,5000774,79};

                for(int i = 0; i< valid.length; i++){
                    assertTrue(stepFunction.checkValidity(valid[i], 2, 100000));
                    assertFalse(stepFunction.checkValidity(invalid[i], 2, 100000));
                }
            }
            if(stepFunction.getName().equals("BlockSize * 2")){
                count ++;
                int[] valid = new int[] {2,4,8,16,128,256,512,2048,1024, 4096};
                int[] invalid = new int[] {5,9,63,81,105,201,1001,3099,5000773,7};
                for(int i = 0; i< valid.length; i++){
                    assertTrue(stepFunction.checkValidity(valid[i], 2, 100000));
                    assertFalse(stepFunction.checkValidity(invalid[i], 2, 100000));
                }
            }
            if(stepFunction.getName().equals("Files + 10")){
                count ++;
                int[] valid = new int[] {22,32,82,162,1282,2562,502,2002,10242,4092};
                int[] invalid = new int[] {5,9,63,81,105,201,1001,3099,5000770,7};
                for(int i = 0; i< valid.length; i++){
                    assertTrue(stepFunction.checkValidity(valid[i], 2, 100000));
                    assertFalse(stepFunction.checkValidity(invalid[i], 2, 100000));
                }
            }
        }
        assertEquals(3, count);
    }

    @Test
    public void testClosestNeighbor(){
        SoftwareSystem softwareSystem = super.getSoftwareSystem2();
        List<StepFunction> stepFunctionList = new ArrayList<>();
        for(NumericOption numericOption: softwareSystem.getNumericOptions()){
            stepFunctionList.add(numericOption.getStepFunction());
        }
        for(StepFunction stepFunction: stepFunctionList){
            if(stepFunction.getName().equals("x + 2")){
                assertEquals(10, stepFunction.findClosestAlternative(11,0,100));
                assertEquals(100, stepFunction.findClosestAlternative(110,0,100));
                assertEquals(2, stepFunction.findClosestAlternative(2,0,100));
            }
            if(stepFunction.getName().equals("Files + 10")){

                assertEquals(10, stepFunction.findClosestAlternative(14,0,100));
                assertEquals(20, stepFunction.findClosestAlternative(16,0,100));
                assertEquals(1, stepFunction.findClosestAlternative(0,1,100));
            }
        }
    }
}
