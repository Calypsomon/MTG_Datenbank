package config;

public class Config {
    public static final String DATA_PATH = "C:/Users/Perin/Documents/PackTheRipper/PTRWebside/data/";
    //Databank
    public static final String PORT = "5432";
    public static final String DATABASE = "postgres";
    public static final String USERNAME = "postgres";
    public static final String PASSWORD = "1234";
    public static final String HOST = "localhost";
    public static final String URL ="jdbc:postgresql://" + HOST + ":" + PORT + "/" + DATABASE;
}