package sa;
import util.Type;
import ts.*;

public interface SaDecVar extends SaNode {
    public String    getNom();
    public Type      getType();
    public TsItemVar getTsItem();
    public void      setTsItem(TsItemVar tsItem);

}
