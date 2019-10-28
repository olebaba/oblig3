package no.oslomet.cs.algdat.Oblig3;

////////////////// ObligSBinTre /////////////////////////////////

import sun.font.CreatedFontTracker;

import java.util.*;

public class ObligSBinTre<T> implements Beholder<T>
{
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
  }
  
  @Override
  public boolean leggInn(T verdi)
  {
    //throw new UnsupportedOperationException("Ikke kodet ennå!");
      Objects.requireNonNull(verdi, "Ulovlig med nullverdier!");

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
    throw new UnsupportedOperationException("Ikke kodet ennå!");
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
      Node<T> p = rot;
      int antallP = 0;

      int cmp = comp.compare(verdi, p.verdi);

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
  
  private static <T> Node<T> nesteInorden(Node<T> p)
  {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
  }
  
  @Override
  public String toString(){
      
      if(tom()){
          return ("[]");
      }
      
      if(rot.høyre == null && rot.venstre == null){
          return ("["+rot+"]");
      }
      
      StringBuilder sb = new StringBuilder();
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
  
  public String omvendtString()
  {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
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
  
  public String postString()
  {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
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
    public void remove()
    {
      throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

  } // BladnodeIterator


  public static void main(String[] args){
      Integer[] a = {4,7,2,9,4,10,8,7,4,6};
      ObligSBinTre<Integer> tre = new ObligSBinTre<>(Comparator.naturalOrder());
      for(int verdi : a) tre.leggInn(verdi);
      System.out.println(tre.antall());      // Utskrift: 10
      System.out.println(tre.antall(5));     // Utskrift: 0
      System.out.println(tre.antall(4));     // Utskrift: 3
      System.out.println(tre.antall(7));     // Utskrift: 2
      System.out.println(tre.antall(10));    // Utskrift: 1
  }

} // ObligSBinTre
