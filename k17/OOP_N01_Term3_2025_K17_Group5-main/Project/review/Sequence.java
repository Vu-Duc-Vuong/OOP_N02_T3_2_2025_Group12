public class Sequence 
{
    private Object[] objects; //array of Object
    private int next = 0;
    public Sequence(int size) 
    { 
        objects = new Object[size];   
    }
      
    public void add(Object x) 
    { //x dont have length
        if (next < objects.length) 
        { 
            objects[next] = x; 
            next++;
        } 
    }
    //inner class SSelector of Sequence
   
    private class SSelector implements Selector 
    {
        //Selector is an interface
        int i = 0;
        public boolean end() 
        { 
            return i == objects.length;
        }
        public Object current() 
        { 
            return objects[i]; 
        }
        public void next() 
        {
            if (i < objects.length) i++;
        }
    }
    public Selector getSelector() 
    { 
        return new  SSelector();
    }
}

