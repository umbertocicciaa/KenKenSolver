package risolutore;

public record Risolutore(RisolviPuzzle risolviPuzzle) {
    public Risolutore {
        if (risolviPuzzle == null) throw new NullPointerException();
    }
    public void risolviKenken() {
        risolviPuzzle.risolvi();
    }

    public boolean trovataSoluzione(){
        return risolviPuzzle.trovataSoluzione();
    }
}
