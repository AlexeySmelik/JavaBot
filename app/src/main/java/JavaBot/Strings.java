package JavaBot;

public enum Strings {
    statistic("statistic"),
    dictionary("dictionary"),
    revise("revise"),
    back("back"),
    learn("learn"),
    again("again"),
    test("test"),
    collectionName("user_list"),
    mainMenuMessage("You are in main state, bro..."),
    helpMessage("""
                In main state you can write these messages:\s
                statistic - print statistic of your learned words\s
                dictionary - print all your learned words\s
                learn - show you new english words\s
                revise - start a test to revise words which have been shown earlier"""),
    printBack("You can write: back - go to main state"),
    printBackOrAgain("""
                        You can write:\s
                        back - go to main state
                        again - learn new words"""),
    printTestOrBackOrAgain("""
                You can write:\s
                test - start test to revise english words
                back - go to main state
                again - learn some new words""");

    public final String string;

    Strings(String string) {
        this.string = string;
    }
}
