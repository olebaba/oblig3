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

        antall++;
        endringer++;
        // én verdi mer i treet
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
            Node<T> b = (p.venstre != null) ? p.venstre : p.høyre;  // b for barn


            if (p == rot) rot = b;
            else if (p == q.venstre){
                q.venstre = b;
                if(b!=null){
                    b.forelder=q;
                }
            }
            else{
                q.høyre = b;
                if(b!=null){
                    b.forelder=q;
                }
            }
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


            if (s != p){
                s.venstre = r.høyre;
                if(r.høyre!=null){
                    r.høyre.forelder=s;
                }
            } else{
                s.høyre = r.høyre;
                if(r.høyre!=null){
                    r.høyre.forelder=s;
                }
            }

        }
        endringer++;
        antall--;   // det er nå én node mindre i treet
        return true;
    }

    public int fjernAlle(T verdi)
    {
        int antallF=0;
        boolean x =true;
        while (x){
            antallF++;
            x=fjern(verdi);
        };
        return antallF-1;
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

      /*int cmp = comp.compare(verdi, p.verdi);

      while (true){
          while (p.venstre != null){
              p = p.venstre;
          }
          if(p.venstre != null){
              p = p.venstre;

          } else if(p.høyre == null) break;
          else {
              p = p.høyre;
          }
      }*/

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
        for (T p: hjelpeliste){
            fjern(p);
        }
    }

    private static <T> Node<T> nesteInorden(Node<T> p) {
        Node<T> q = p;

        if (q.forelder == null) {
            if (q.høyre != null) {
                q = q.høyre;
                while (q.venstre != null) {
                    q = q.venstre;
                }
                return q;
            }
            return null;
        }

        if (q.høyre != null) {
            q = q.høyre;
            while (q.venstre != null) {
                q = q.venstre;
            }
            return q;
        }

        if (q.forelder.venstre == p) return q.forelder;

        while (q.forelder != null && q.forelder.høyre == q) q = q.forelder;

        return q.forelder;
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
      Stakk<Node<T>> stakk = new TabellStakk<>();
      Node<T> c = rot;

      while(c.venstre != null){
          c = c.venstre;
      }
      while(nesteInorden(c) != null){
          stakk.leggInn(c);
          c = nesteInorden(c);
      }

      stakk.leggInn(c);

//      StringBuilder sb = new StringBuilder();
//      sb.append("[");



//      while(true){
//
//          if(t == 1){
//              sb.append(c.verdi + "]");
//              break;
//          }
//          else{
//              sb.append(c.verdi + ", ");
//              t--;
//          }
//      }

//      for(; c.høyre != null; c = c.høyre){
//          stakk.leggInn(c);
//      }
//
//      if(c.venstre != null){
//
//          for(c = c.venstre; c.høyre != null; c = c.høyre){
//              stakk.leggInn(c);
//          }
//      }
//      else{
//          c = stakk.taUt();
//      }
//
//      sb.append("]");
      return stakk.toString();
  }

    public String høyreGren()
    {
        if(tom()){
            return("[]");
        }
        StringBuilder sb = new StringBuilder("[");
        Node<T> c = rot;
        sb.append(c.verdi);
        while (true){
            if(c.høyre!=null){
                c=c.høyre;

            }else if(c.venstre!=null){
                c=c.venstre;

            }else {
                break;
            }
            sb.append(", ").append(c.verdi);
        }
        sb.append("]");
        return sb.toString();
    }

    public String lengstGren()
    {

        if (tom()) return "[]";

        Kø<Node<T>> ko = new TabellKø<>();
        Node<T> c = rot;
        ko.leggInn(c);

        while (!ko.tom())
        {
            c = ko.taUt();
            if (c.høyre != null) ko.leggInn(c.høyre);
            if (c.venstre != null) ko.leggInn(c.venstre);

        }
        Stakk<Node<T>> hstakk = new TabellStakk<>();
        while (c!=null){
            hstakk.leggInn(c);
            c=c.forelder;
        }
        return hstakk.toString();

    }

    public String[] grener()
    {
        if (tom()) return new String[]{};
        if (antall()==1) return new String[]{"["+ rot.verdi.toString()+"]"};

        Liste<Node<T>> list=new TabellListe<>();
        Liste<Node<T>> bladlist=new TabellListe<>();
        Stakk<Node<T>> hstakk = new TabellStakk<>();

        Node<T> c = rot;
        while (c.venstre!=null){
            c=c.venstre;
        }
        while (nesteInorden(c)!=null){
            list.leggInn(c);
            c=nesteInorden(c);
        }
        list.leggInn(c);

        for(Node<T> i:list){
            if(i.venstre==null && i.høyre==null){
                bladlist.leggInn(i);
            }
        }

        int l=bladlist.antall();
        String[] ut=new String[l];
        int e=0;
        for (Node<T> f:bladlist){
            while (f!=null){
                hstakk.leggInn(f);
                f=f.forelder;
            }
            ut[e]=hstakk.toString();
            e++;
            hstakk.nullstill();
        }
        
        return ut;
    }

    public String bladnodeverdier()
    {
        Liste<Node<T>> list=new TabellListe<>();

        return blhj(rot,list).toString();

    }
    public Liste<Node<T>> blhj(Node<T> p,Liste<Node<T>> list){

        if(p!=null){
            if(p.høyre==null && p.venstre==null){
                list.leggInn(p);
            }
            blhj(p.venstre,list);
            blhj(p.høyre,list);
        }

        return list;
    }

    public String postString(){
          
      if(tom()){
          return ("[]");
      }
      if(antall == 1){
          return ("[" + rot + "]");
      }
      StringBuilder sb=new StringBuilder("[");
      //første
      Node<T> p = rot;
      while (true){
          if(p.venstre!=null) p=p.venstre;
          else if(p.høyre!=null) p=p.høyre;
          else break;
      }
      sb.append(p.verdi);

      while (p!=rot){
          if(p==p.forelder.høyre){
              p=p.forelder;
              sb.append(", ").append(p.verdi);
          }else if(p==p.forelder.venstre){
              if(p.forelder.høyre==null){
                  p=p.forelder;
                  sb.append(", ").append(p.verdi);
              }else {
                  p=p.forelder.høyre;
                  while (true){
                      if(p.venstre!=null) p=p.venstre;
                      else if(p.høyre!=null) p=p.høyre;
                      else break;
                  }

                  sb.append(", ").append(p.verdi);
              }
          }
      }
      sb.append("]");

      return sb.toString();
    }

    @Override
    public Iterator<T> iterator()
    {
        return new BladnodeIterator();
    }

    private class BladnodeIterator implements Iterator<T> {
        private Node<T> p = rot, q = null;
        private boolean removeOK = false;
        private int iteratorendringer = endringer;
        private Kø<Node<T>> ko = new TabellKø<>();

        private BladnodeIterator()  // konstruktør
        {

            if (!tom()) {

                Node<T> c = rot;

                while (p.venstre != null) {
                    p = p.venstre;
                }
                while (true) {

                    if (p.høyre == null && p.venstre == null) {
                        break;
                    }

                    p = nesteInorden(p);
                }

                q = p;
            }
        }

        @Override
        public boolean hasNext()
        {
            return p != null;  // Denne skal ikke endres!
        }

        @Override
        public T next(){

            if(!hasNext()){
                throw new NoSuchElementException();
            }
            if(endringer != iteratorendringer){
                throw new ConcurrentModificationException("Iteratorendringer er "+iteratorendringer+" og "+
                        endringer);
            }

            T value = p.verdi;
            q = p;

            p=nesteInorden(p);

            while(p != null && (p.venstre != null || p.høyre != null)){
                p = nesteInorden(p);
            }
            removeOK = true;
            return value;
        }
        
        /*
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
        */
        
        @Override
        public void remove(){
            
            if(!removeOK){
                throw new IllegalStateException("feil ved fjerning");
            }
            
            if(q.forelder != null){
                
                if(q.forelder.venstre == q){
                    
                    q.forelder.venstre = null;
                    
                    q.forelder = null;
                }
                else{
                    
                    q.forelder.høyre = null;
                    
                    q.forelder = null;
                }
            }

            
            removeOK = false;
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

         int[] a = {4,7,2,9,4,10,8,7,4,6,1};
         ObligSBinTre<Integer> tre = new ObligSBinTre<>(Comparator.naturalOrder());
         for(int verdi : a) tre.leggInn(verdi);

         //test(tre);
         System.out.println(tre);  // [1, 2, 4, 4, 4, 6, 7, 7, 8, 9, 10]

        /*int[] a = {4,7,2,9,4,10,8,7,4,6,1};

        ObligSBinTre<Integer> tre = new ObligSBinTre<>(Comparator.naturalOrder());
        for(int verdi : a) tre.leggInn(verdi);
        //System.out.println(tre.fjernAlle(4));  // 3   tre.fjernAlle(7); tre.fjern(8);
        System.out.println(tre.antall());  // 5
        System.out.println(tre + "​ ​"+tre.toString());   // [1, 2, 6, 9, 10] [10, 9, 6, 2, 1]
        // OBS: Hvis du ikke har gjort oppgave 4 kan du her bruke toString()*/
    }

} // ObligSBinT
