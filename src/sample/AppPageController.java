package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

public class AppPageController {
    private Tomasulo architecture;
    @FXML
    Label clkLabel;
    @FXML
    GridPane instructionQ;
    @FXML
    GridPane addRs;
    @FXML
    GridPane mulRs;
    @FXML
    GridPane loadBuff;
    @FXML
    GridPane storeBuff;
    @FXML
    GridPane regFile;
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void setTomasulo(int addLatency, int subLatency, int mulLatency, int divLatency, int loadLatency, int storeLatency, String program) {
        architecture = new Tomasulo(addLatency, subLatency, mulLatency, divLatency, loadLatency, storeLatency, program);
    }

    @FXML
    private void onNextClick(ActionEvent event) throws IOException {
        if (architecture.isDone()) {
            clkLabel.setText("Congrats! You are done!");
        } else {
            architecture.performCycle();
            clkLabel.setText("Clock cycle: " + architecture.currentCycle);
            setInstructionQ();
            setRegFile();
            setAddRs();
            setLoadBuff();
            setStoreBuff();

        }
    }

    public void setInstructionQ() {
        for (int i = 2; i <= architecture.instrucionQueue.length + 1; i++) {
            Instruction ins = architecture.instrucionQueue[i - 2];
            ((Label) instructionQ.getChildren().get(i )).setText(ins.opCode.name());
            if (ins.destination != -1) {
                ((Label) instructionQ.getChildren().get(i + 10)).setText("F" + ins.destination);
            }
            if (ins.source1 != -1) {
                Label source1 = new Label();
                if (ins.opCode == OpCode.LOAD || ins.opCode == OpCode.STORE) {
                    source1.setText("MEM[" + ins.source1 + "]");
                } else {
                    source1.setText("F" + ins.source1);
                }
                ((Label) instructionQ.getChildren().get(i + 2 * 10)).setText(source1.getText());

            }
            if (ins.opCode != OpCode.LOAD && ins.opCode != OpCode.STORE) {
                ((Label) instructionQ.getChildren().get(i + 10 * 3)).setText("F" + ins.source2);

            }
            if (ins.issueCycle != -1) {

                ((Label) instructionQ.getChildren().get(i + 10 * 4)).setText(ins.issueCycle + "");

            }
            if (ins.startCycle != -1) {
                ((Label) instructionQ.getChildren().get(i + 10 * 5)).setText(ins.startCycle + "");
            }
            if (ins.endCycle != -1) {
                ((Label) instructionQ.getChildren().get(i + 10 * 6)).setText(ins.endCycle + "");
            }
            if (ins.writeCycle != -1) {
                ((Label) instructionQ.getChildren().get(i + 10 * 7)).setText(ins.writeCycle + "");

            }
        }
    }


    public void setRegFile() {
        for (int i = 1; i <= architecture.registerFile.length; i++) {
            Register r = architecture.registerFile[i - 1];
            Label val = new Label(r.val + "");
            ((Label) regFile.getChildren().get(33 + i + 1)).setText(val.getText());

            Label qi = new Label(r.qI.name() + "");
            ((Label) regFile.getChildren().get(2 * 33 + i + 1)).setText(qi.getText());

        }
    }

    public void setAddRs() {
        for (int i = 1; i <= architecture.addStation.length; i++) {
            ReservationStation r = architecture.addStation[i - 1];
            if (r.busy) {
                ((Label) addRs.getChildren().get(i + 4 + 1)).setText(r.opCode.name());
                if (r.vJ != -1) {
                    ((Label) addRs.getChildren().get(i + 4 * 2 + 1)).setText(r.vJ + " ");
                }
                if (r.vK != -1) {
                    ((Label) addRs.getChildren().get(i + 4 * 3 + 1)).setText(r.vK + " ");
                }

                ((Label) addRs.getChildren().get(i + 4 * 4 + 1)).setText(r.qJ.name());
                ((Label) addRs.getChildren().get(i + 4 * 5 + 1)).setText(r.qK.name());
                ((Label) addRs.getChildren().get(i + 4 * 6 + 1)).setText(r.busy + "");
                if (r.remainingCycles != -1)
                    ((Label) addRs.getChildren().get(i + 4 * 7 + 1)).setText(r.remainingCycles + "");
            } else {
                ((Label) addRs.getChildren().get(i + 4 + 1)).setText("");
                ((Label) addRs.getChildren().get(i + 4 * 2 + 1)).setText("");
                ((Label) addRs.getChildren().get(i + 4 * 3 + 1)).setText("");
                ((Label) addRs.getChildren().get(i + 4 * 4 + 1)).setText("");
                ((Label) addRs.getChildren().get(i + 4 * 5 + 1)).setText("");
                ((Label) addRs.getChildren().get(i + 4 * 6 + 1)).setText("false");
                ((Label) addRs.getChildren().get(i + 4 * 7 + 1)).setText("");
            }
        }
    }

    public void setMulRs() {
        for (int i = 1; i <= architecture.mulStation.length; i++) {
            ReservationStation r = architecture.mulStation[i - 1];
            if (r.busy) {
                ((Label) mulRs.getChildren().get(i + 4 + 1)).setText(r.opCode.name());
                if (r.vJ != -1) {
                    ((Label) mulRs.getChildren().get(i + 4 * 2 + 1)).setText(r.vJ + " ");
                }
                if (r.vK != -1) {
                    ((Label) mulRs.getChildren().get(i + 4 * 3 + 1)).setText(r.vK + " ");
                }

                ((Label) mulRs.getChildren().get(i + 4 * 4 + 1)).setText(r.qJ.name());
                ((Label) mulRs.getChildren().get(i + 4 * 5 + 1)).setText(r.qK.name());
                ((Label) mulRs.getChildren().get(i + 4 * 6 + 1)).setText(r.busy + "");
                if (r.remainingCycles != -1)
                    ((Label) mulRs.getChildren().get(i + 4 * 7 + 1)).setText(r.remainingCycles + "");
            } else {
                ((Label) mulRs.getChildren().get(i + 4 + 1)).setText("");
                ((Label) mulRs.getChildren().get(i + 4 * 2 + 1)).setText("");
                ((Label) mulRs.getChildren().get(i + 4 * 3 + 1)).setText("");
                ((Label) mulRs.getChildren().get(i + 4 * 4 + 1)).setText("");
                ((Label) mulRs.getChildren().get(i + 4 * 5 + 1)).setText("");
                ((Label) mulRs.getChildren().get(i + 4 * 6 + 1)).setText("false");
                ((Label) mulRs.getChildren().get(i + 4 * 7 + 1)).setText("");
            }
        }
    }

    public void setLoadBuff() {
        for (int i = 1; i <= architecture.loadStation.length; i++) {
            LoadBuffer r = architecture.loadStation[i - 1];
            if (r.busy) {
                if (r.address != -1) {
                    ((Label) loadBuff.getChildren().get(i + 4 + 1)).setText(r.address + " ");
                }


                ((Label) loadBuff.getChildren().get(i + 4 * 2 + 1)).setText(r.busy + "");
                if (r.remainingCycles != -1)
                    ((Label) loadBuff.getChildren().get(i + 4 * 3 + 1)).setText(r.remainingCycles + "");
            } else {
                ((Label) loadBuff.getChildren().get(i + 4 + 1)).setText("");
                ((Label) loadBuff.getChildren().get(i + 4 * 2 + 1)).setText("false");
                ((Label) loadBuff.getChildren().get(i + 4 * 3 + 1)).setText("");

            }
        }
    }

    public void setStoreBuff() {
        for (int i = 1; i <= architecture.storeStation.length; i++) {
            StoreBuffer r = architecture.storeStation[i - 1];
            if (r.busy) {
                if (r.address != -1) {
                    ((Label) storeBuff.getChildren().get(i + 1)).setText(r.address + " ");
                }

                if (r.val != -1)
                    ((Label) storeBuff.getChildren().get(i + 4 + 1)).setText(r.val + "");
                ((Label) storeBuff.getChildren().get(i + 4*2 + 1)).setText(r.qI.name());
                ((Label) storeBuff.getChildren().get(i + 4*3 + 1)).setText(r.busy + "");

                if (r.remainingCycles != -1)
                    ((Label) storeBuff.getChildren().get(i + 4 * 4 + 1)).setText(r.remainingCycles + "");
            } else {
                ((Label) storeBuff.getChildren().get(i +1)).setText("");
                ((Label) storeBuff.getChildren().get(i + 4  + 1)).setText("");
                ((Label) storeBuff.getChildren().get(i + 4*2  + 2)).setText("");
                ((Label) storeBuff.getChildren().get(i + 4 *3 + 1)).setText("false");
                ((Label) storeBuff.getChildren().get(i + 4 *4 + 1)).setText("");
            }
        }

    }
}
