package logic.inicializacion;

public class InitializationFactoryImp extends InitializationFactory{

    @Override
    public InitializationMethod getInitializationMethod(int index) {
        switch (index){
            case 0:
                return new GrowthInitialization();
            case 1:
                return new CompleteInitialization();
            case 2:
                return new RampedAndHalfInitialization();
            default:
                return new GrowthInitialization();
        }
    }

}
