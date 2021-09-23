public class Main {
    public static void main(String [] args) {
        var i = new String("111");
        var o = (Object) i;
        System.out.println(o);
        var bot = new Bot();
        bot.startPolling();
    }
}
