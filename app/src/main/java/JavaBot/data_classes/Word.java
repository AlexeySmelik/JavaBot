package JavaBot.data_classes;

public class Word {
    private final String heading;
    private final String translation;

    public Word(String heading, String translation) {
        this.heading = heading;
        this.translation = translation;
    }

    public String getHeading(){
        return heading;
    }

    public String getTranslation(){
        return translation;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        var word = (Word) obj;
        return word.hashCode() == this.hashCode();
    }

    @Override
    public int hashCode() {
        return (this.heading + this.translation).hashCode();
    }
}
