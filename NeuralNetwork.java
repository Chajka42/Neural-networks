import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class NeuralNetwork {
    private static final int INPUT_NODES = 4000;
    private static final int HIDDEN_NODES = 10;
    private static final int OUTPUT_NODES = 1;
    private static final double LEARNING_RATE = 0.1;

    private double[] inputs;
    private double[][] inputWeights;
    private double[] hiddenValues;
    private double[][] hiddenWeights;
    private double[] outputValues;
    private double[] expectedOutputs;
    private Random random;
    
    public static double [][] arrayvolt = new double [3][2002];
    public static double[][] inIN = new double[8000][4004];
    public static double[] inAN = new double[8000];
    public static double[][] inKJ = new double[1000][4004];

    public static void main(String[] args) {

        int n = 0, ncase = 1, incase, nn;
        

        int countFiles = 0;
        File f= new File("/readtek5");
        File[] files = f.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                countFiles++;  
            }
        }


        while (ncase <= (countFiles / 3)){ 

            incase = 0;


            while (incase < 3) {
                
            
             

             String pathto = "readtek5/C0Trace00000.csv";
 
         
         String strokafirst;  
 
         n = incase + 1;
         nn = ncase - 1; 
  
     if (nn < 10000 && nn > 999) {strokafirst = "" + nn;} else {   if (nn < 1000 && nn > 99) {strokafirst = "0" + nn;} else { if (nn < 100 && nn > 9) { strokafirst = "00" + nn;} else { strokafirst = "000" + nn;}} }

     pathto = pathto.substring(0,10) + n + pathto.substring(11,17) + strokafirst + pathto.substring(21,25); 
    

           try {
            File file = new File("/" + pathto);
            FileReader fr = new FileReader(file);
            try (
            BufferedReader reader = new BufferedReader(fr)) {
                String line = reader.readLine();
                int i = 0;
                while (line != null) {
                    i++;
                    if (i > 5 && i < 2008) { 
                        int index1 = line.indexOf(',');
                        String line2 = line.substring(index1 + 1,line.length()); 
                        try{
                            Double dino = Double.parseDouble(line2); 
                            
                            
                            arrayvolt [incase][i-6] = dino;
                           if (ncase < 8000){
                            if(incase == 0){
                           inIN[ncase-1][i-6] = dino; }
                           if(incase == 1){
                            inIN[ncase-1][i-6+2002] = dino; }}else{
                                if(incase == 0){
                                    inIN[ncase-1][i-6] = dino; }
                                    if(incase == 1){
                                     inIN[ncase-1][i-6+2002] = dino; }
                            }


                        }
                        catch (NumberFormatException ex){
                            ex.printStackTrace(); 
                        }
                    }
                    line = reader.readLine();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
             
              incase += 1;
             }}



             try {
                File file = new File("C:/!output.txt");
                FileReader fr = new FileReader(file);
                try (
                BufferedReader reader = new BufferedReader(fr)) {
                    String line = reader.readLine();
                    int i = 0;
                    while (line != null) {
                        i++;
                        if (i > 0 && i < 9913) { 
                           String[] splited = line.split(" ");
                          
                            try{
                               
                                
                                inAN[i] = Double.parseDouble(splited[2]);
                              
                               
                            }
                            catch (NumberFormatException ex){
                                ex.printStackTrace(); 
                            }
                        }
                        line = reader.readLine();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        NeuralNetwork nnnn = new NeuralNetwork();
        

        
        // Train the neural network
        nnnn.train(inIN, inAN);
        
       
    for(int b = 0; b < 1000; b++){
        double[] predictedOutput = nnnn.predict(inKJ[b]);
        System.out.println("Predicted neutron flux: " + predictedOutput[0]);
     } }



    public NeuralNetwork() {
        inputs = new double[INPUT_NODES];
        inputWeights = new double[INPUT_NODES][HIDDEN_NODES];
        hiddenValues = new double[HIDDEN_NODES];
        hiddenWeights = new double[HIDDEN_NODES][OUTPUT_NODES];
        outputValues = new double[OUTPUT_NODES];
        expectedOutputs = new double[OUTPUT_NODES];
        random = new Random();

        for (int i = 0; i < INPUT_NODES; i++) {
            for (int j = 0; j < HIDDEN_NODES; j++) {
                inputWeights[i][j] = random.nextDouble();
            }
        }

        for (int i = 0; i < HIDDEN_NODES; i++) {
            for (int j = 0; j < OUTPUT_NODES; j++) {
                hiddenWeights[i][j] = random.nextDouble();
            }
        }
    }

    public void train(double[][] inputData, double[] outputData) {
        for (int i = 0; i < inputData.length; i++) {
            inputs = inputData[i];
            expectedOutputs[0] = outputData[i];
            feedForward();
            backPropagation();
        }
    }

    private void feedForward() {
        for (int i = 0; i < HIDDEN_NODES; i++) {
            double sum = 0;
            for (int j = 0; j < INPUT_NODES; j++) {
                sum += inputs[j] * inputWeights[j][i];
            }
            hiddenValues[i] = sigmoid(sum);
        }

        for (int i = 0; i < OUTPUT_NODES; i++) {
            double sum = 0;
            for (int j = 0; j < HIDDEN_NODES; j++) {
                sum += hiddenValues[j] * hiddenWeights[j][i];
            }
            outputValues[i] = sigmoid(sum);
        }
    }


    private void backPropagation() {
        double[] outputErrors = new double[OUTPUT_NODES];
        for (int i = 0; i < OUTPUT_NODES; i++) {
            double delta = expectedOutputs[i] - outputValues[i];
            outputErrors[i] = delta * sigmoidDerivative(outputValues[i]);
        }

        double[] hiddenErrors = new double[HIDDEN_NODES];
        for (int i = 0; i < HIDDEN_NODES; i++) {
            double delta = 0;
            for (int j = 0; j < OUTPUT_NODES; j++) {
                delta += outputErrors[j] * hiddenWeights[i][j];
            }
            hiddenErrors[i] = delta * sigmoidDerivative(hiddenValues[i]);
        }

        for (int i = 0; i < HIDDEN_NODES; i++) {
            for (int j = 0; j < OUTPUT_NODES; j++) {
                hiddenWeights[i][j] += LEARNING_RATE * hiddenErrors[i] * hiddenValues[i];
            }
        }

        for (int i = 0; i < INPUT_NODES; i++) {
            for (int j = 0; j < HIDDEN_NODES; j++) {
                inputWeights[i][j] += LEARNING_RATE * hiddenErrors[j] * inputs[i];
            }
        }
    }

    public double[] predict(double[] input) {
        inputs = input;
        feedForward();
        return outputValues;
    }

    private static double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    private static double sigmoidDerivative(double x) {
        return x * (1 - x);
    }
}