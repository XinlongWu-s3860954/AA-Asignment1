import java.io.PrintWriter;
import java.util.*;

/**
 * SIR model.
 *
 * @author Jeffrey Chan, 2021.
 */
public class SIRModel
{
    int maxRandomValue = 100;
    Map infected;
    Map recovered;
    Map newInfected;
    Map newRecover;
    /**
     * Default constructor, modify as needed.
     */
    public SIRModel() {
        infected = new HashMap<String,SIRModel>();
        recovered = new HashMap<String,SIRModel>();
        newInfected = new HashMap<String,SIRModel>();
        newRecover = new HashMap<String,SIRModel>();
    } // end of SIRModel()


    /**
     * Run the SIR epidemic model to completion, i.e., until no more changes to the states of the vertices for a whole iteration.
     *
     * @param graph Input contracts graph.
     * @param seedVertices Set of seed, infected vertices.
     * @param infectionProb Probability of infection.
     * @param recoverProb Probability that a vertex can become recovered.
     * @param sirModelOutWriter PrintWriter to output the necessary information per iteration (see specs for details).
     */
    public void runSimulation(ContactsGraph graph, String[] seedVertices,
        float infectionProb, float recoverProb, PrintWriter sirModelOutWriter)
    {
        // IMPLEMENT ME!
        for (String ver : seedVertices) {
            infected.put(ver,SIRState.I);
        }
        Random r = new Random();

        int count = 1;
        while(infected.size()>0){
            infected.forEach((vertex,SIR)->{
                // infection
                String susceptibleList[] =  graph.kHopNeighbours(1,(String)vertex);
                for (String susceptibleVertex : susceptibleList) {
                    int random = r.nextInt(maxRandomValue);
                    if(!recovered.containsKey(susceptibleVertex)
                            &&!infected.containsKey(susceptibleVertex)
                            &&random<maxRandomValue*infectionProb){
                        graph.toggleVertexState(susceptibleVertex);
                        newInfected.put(susceptibleVertex,SIRState.I);
                    }
                }

                // recover
                int random = r.nextInt(maxRandomValue);
                if(random<maxRandomValue*recoverProb){
                    graph.toggleVertexState((String)vertex);
                    newRecover.put(vertex,SIRState.R);
                }
            });

            infected.putAll(newInfected);
            newRecover.forEach((vertex,SIR)->{
                infected.remove(vertex);
            });
            recovered.putAll(newRecover);
            sirModelOutWriter.println(count+": "+ getPrintString(newInfected) + " : " + getPrintString(newRecover));
            newInfected.clear();
            newRecover.clear();
            count++;
        }
    } // end of runSimulation()

    private String getPrintString(Map map){
        String out = "[";
        String[] para = (String[]) map.keySet().toArray(new String[0]);
        for (int i = 0; i < para.length; i++) {
            out += para[i];
            if(i<para.length-1){
                out+=" ";
            }
        }
        out += "]";
        return out;
    }
} // end of class SIRModel
