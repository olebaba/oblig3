package no.oslomet.cs.algdat.Oblig3;

////////////////// ObligSBinTre /////////////////////////////////

import sun.font.CreatedFontTracker;

import java.util.*;

public class ObligSBinTre<T> implements Beholder<T>
{
    private TabellStakk<T> hjelpestakk; //hjelpevariabel
    private TabellListe<T> hjelpeliste;
    private static final class Node<T>   // en indre nodeklasse
    {
        private T verdi;                   // nodens verdi
        private Node<T> venstre, høyre;    // venstre og høyre barn
        private Node<T> forelder;          // forelder

        // konstruktør
        private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder)
        {
            this.verdi = verdi;
            venstre = v; høyre = h;
            this.forelder = forelder;
        }

        private Node(T verdi, Node<T> forelder)  // konstruktør
        {
            this(verdi, null, null, forelder);
        }

        @Override
        public String toString(){ return "" + verdi;}

    } // class Node

    private Node<T> rot;                            // peker til rotnoden
    private int antall;                             // antall noder
    private int endringer;                          // antall endringer

    private final Comparator<? super T> comp;       // komparator

    public ObligSBinTre(Comparator<? super T> c)    // konstruktør
    {
        rot = null;
        antall = 0;
        comp = c;
        hjelpestakk=new TabellStakk<>();
        hjelpeliste=new TabellListe<>();
    }

    @Override
    public boolean leggInn(T verdi)
    {
        //throw new UnsupportedOperationException("Ikke kodet ennå!");
        Objects.requireNonNull(verdi, "Ulovlig med nullverdier!");
        hjelpestakk.leggInn(verdi);
        hjelpeliste.leggInn(verdi);

        Node<T> p = rot, q = null;               // p starter i roten
        int cmp = 0;                             // hjelpevariabel

        while (p != null)       // fortsetter til p er ute av treet
        {
            q = p;                                 // q er forelder til p
            cmp = comp.compare(verdi,p.verdi);     // bruker komparatoren
            p = cmp < 0 ? p.venstre : p.høyre;     // flytter p
        }

        // p er nå null, dvs. ute av treet, q er den siste vi passerte

        p = new Node<>(verdi, q);                   // oppretter en ny node

        if (q == null) rot = p;                  // p blir rotnode
        else if (cmp < 0) q.venstre = p;         // venstre barn til q
        else q.høyre = p;                        // høyre barn til q

        antall++;                                // én verdi mer i treet
        return true;                             // vellykket innlegging
    }

    @Override
    public boolean inneholder(T verdi)
    {
        if (verdi == null) return false;

        Node<T> p = rot;

        while (p != null)
        {
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0) p = p.venstre;
            else if (cmp > 0) p = p.høyre;
            else return true;
        }

        return false;
    }

    @Override
    public boolean fjern(T verdi)
    {
        if (verdi == null) return false;  // treet har ingen nullverdier

        Node<T> p = rot, q = null;   // q skal være forelder til p

        while (p != null)            // leter etter verdi
        {
            int cmp = comp.compare(verdi,p.verdi);      // sammenligner
            if (cmp < 0) { q = p; p = p.venstre; }      // går til venstre
            else if (cmp > 0) { q = p; p = p.høyre; }   // går til høyre
            else break;    // den søkte verdien ligger i p
        }
        if (p == null) return false;   // finner ikke verdi

        if (p.venstre == null || p.høyre == null)  // Tilfelle 1) og 2)
        {
            Node<T> b = p.venstre != null ? p.venstre : p.høyre;  // b for barn
            if (p == rot) rot = b;
            else if (p == q.venstre) q.venstre = b;
            else q.høyre = b;
        }
        else  // Tilfelle 3)
        {
            Node<T> s = p, r = p.høyre;   // finner neste i inorden
            while (r.venstre != null)
            {
                s = r;    // s er forelder til r
                r = r.venstre;
            }

            p.verdi = r.verdi;   // kopierer verdien i r til p

            if (s != p) s.venstre = r.høyre;
            else s.høyre = r.høyre;
        }

        antall--;   // det er nå én node mindre i treet
        return true;
    }

    public int fjernAlle(T verdi)
    {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    @Override
    public int antall()
    {
        return antall;
    }

    public int antall(T verdi)
    {
        //throw new UnsupportedOperationException("Ikke kodet ennå!");
        if(!inneholder(verdi)) return 0;
        //Node<T> p = rot;
        int antallP = 0;
        for (T n : hjelpeliste) {
            if (n.equals(verdi)) antallP++;
        }

        return antallP;
    }

    @Override
    public boolean tom()
    {
        return antall == 0;
    }

    @Override
    public void nullstill()
    {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    private static <T> Node<T> nesteInorden(Node<T> p) {
        Node<T> q = p;
        int test = 0, test2 = 0, test3 = 0, test4 = 0, test5 = 0;

        if(q.forelder == null){
            System.out.println("test0");
            if(q.høyre != null) {
                q = q.høyre;
                while (q.venstre != null) {
                    q = q.venstre;
                }
                return q;
            }else {
                return null;
            }
        }

        if (q.høyre != null && q.venstre == null){
            q = q.høyre;
            System.out.println("test" + ++test);
            while (q.venstre != null){
                q = q.venstre;
                System.out.println("test2" + ++test2);
            }
            return q;
        }
        if(q.forelder.venstre == p){
            q = q.forelder;
            System.out.println("test3" + ++test3);
            return q;
        }else {
            if(q.høyre == null && q.venstre == null){
                System.out.println("test6");
                return q.forelder.forelder;
            }
            q = q.forelder;
            System.out.println("test4" + ++test4);
            if(q.høyre != null && q.høyre != p) {
                if (q.forelder != null) {
                    q = q.forelder;
                    System.out.println("test5" + ++test5);
                    return q;
                } else {
                    System.out.println("test5.1 ");
                    q = q.høyre;
                    while (q.venstre != null) {
                        q = q.venstre;
                    }
                    return q;
                }
            }
            return null;

        }
    }

    @Override
    public String toString(){

        if(tom()){
            return("[]");
        }

        if(rot.høyre == null && rot.venstre == null){
            return ("["+rot+"]");
        }

        StringBuilder sb = new StringBuilder("[");
        Node<T> c = rot;

        while(c.venstre != null){
            c = c.venstre;
        }
        while(nesteInorden(c) != null){
            sb.append(c.verdi + ", ");
            c = nesteInorden(c);
        }

        sb.append(c.verdi + "]");
        return sb.toString();
    }

  public String omvendtString(){
      
      if(tom()){
          return ("[]");
      }
      if(antall == 1){
          return ("["+rot+"]");
      }
      
      int t = antall;
      TabellStakk<Node<T>> stakk = new TabellStakk<>();
      
      StringBuilder sb = new StringBuilder();
      sb.append("[");
      
      Node<T> c = rot;
      
      while(t>0){
          
          if(t == 1){
              sb.append(c.verdi + "]");
          }
          else{
              sb.append(c.verdi + ", ");
              t--;
          }
      }
      
      for(; c.høyre != null; c = c.høyre){
          stakk.leggInn(c);
      }
      
      if(c.venstre != null){
          
          for(c = c.venstre; c.høyre != null; c = c.høyre){
              stakk.leggInn(c);
          }
      }
      else{
          c = stakk.taUt();
      }
      
      sb.append("]");
      return sb.toString();
  }

    public String høyreGren()
    {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String lengstGren()
    {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String[] grener()
    {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String bladnodeverdier()
    {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String postString(){
          
      if(tom()){
          return ("[]");
      }
      if(antall == 1){
          return ("[" + rot + "]");
      }
    }

    @Override
    public Iterator<T> iterator()
    {
        return new BladnodeIterator();
    }

    private class BladnodeIterator implements Iterator<T>
    {
        private Node<T> p = rot, q = null;
        private boolean removeOK = false;
        private int iteratorendringer = endringer;

        private BladnodeIterator()  // konstruktør
        {
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }

        @Override
        public boolean hasNext()
        {
            return p != null;  // Denne skal ikke endres!
        }

        @Override
        public T next()
        {
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }

        @Override
        public void remove(){
            
            if(!removeOK){
                throw new IllegalStateException("feil ved fjerning");
            }
            
            removeOK = false;
            
            if(q.forelder != null){
                
                if(q.forelder.venstre == q){
                    
                    q.forelder.verdi = null;
                    
                    q.forelder = null;
                }
                else{
                    
                    q.forelder.høyre = null;
                    
                    q.forelder = null;
                }
            }
            else{
                    rot = null;
                    
                    System.out.println("");
                }
            antall--;
            iteratorendringer++;
            endringer++;
        }

    } // BladnodeIterator

    public static void test(ObligSBinTre tre){
    Node p = tre.rot;
    while (p.venstre != null) p = p.venstre;
        System.out.println(p);
    while (nesteInorden(p) != null){
        System.out.println(nesteInorden(p));
        p=nesteInorden(p);
    }

}

    public static void main(String[] args){

         int[] a = {3,5,6,3,2,1,4,5};
         ObligSBinTre<Integer> tre = new ObligSBinTre<>(Comparator.naturalOrder());
         for(int verdi : a) tre.leggInn(verdi);

         test(tre);
         //System.out.println(tre);  // [1, 2, 4, 4, 4, 6, 7, 7, 8, 9, 10]

        /*int[] a = {4,7,2,9,4,10,8,7,4,6,1};

        ObligSBinTre<Integer> tre = new ObligSBinTre<>(Comparator.naturalOrder());
        for(int verdi : a) tre.leggInn(verdi);
        //System.out.println(tre.fjernAlle(4));  // 3   tre.fjernAlle(7); tre.fjern(8);
        System.out.println(tre.antall());  // 5
        System.out.println(tre + "​ ​"+tre.toString());   // [1, 2, 6, 9, 10] [10, 9, 6, 2, 1]
        // OBS: Hvis du ikke har gjort oppgave 4 kan du her bruke toString()*/
    }

} // ObligSBinTre
