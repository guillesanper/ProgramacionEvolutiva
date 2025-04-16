package logic.inicializacion;

public abstract class InitializationFactory {

    private static InitializationFactory instance;

    public static InitializationFactory getInstance() {
        if (instance == null) {
            instance = new InitializationFactoryImp();
        }
        return instance;
    }

    public abstract InitializationMethod getInitializationMethod(int index);

}
