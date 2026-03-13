package ts;
import util.Type;

public abstract class TsItemVar {
    public Ts      portee;
    public int     adresse;
    public String  identif;
    public int     taille;
    public boolean isParam;
    public Type    type;

    public int    getAdresse(){return this.adresse;}
    public String getIdentif(){return this.identif;}
    public Ts     getPortee() {return this.portee;}
    public Type   getType()   {return this.type;}
    public int    getTaille(){return this.taille;}
    
    
}

