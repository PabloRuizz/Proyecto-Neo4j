/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pruebaneo5;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import static org.neo4j.driver.Values.parameters;

/*
Crear relacciones en el neo:
MATCH(p:producto) WHERE p.codigo="P001" 
MATCH(t:tienda)WHERE t.nombre="Ferreteria Paco" 
CREATE (p)-[:DISPONIBLE{precio:850}]->(t)

Borrar todo en neo:
MATCH(r) DETACH DELETE r
 */
public class Neo4 implements AutoCloseable {
    private Driver driver;

    public Neo4(String uri, String user, String password) {
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }
    
    /* Crear un producto y relacionarlo con una marca y una categoria****************************************
MATCH (m:marca) where m.nombre="HP"
MATCH (c:categoria) where c.nombre="Ferreteria"
CREATE (p:producto{codigo:"P001", descripcion:"Portatil I7-9350 8GB"}) 
CREATE(p)-[r1:ES{}]->(m)
CREATE(p)-[r2:PERTENECE{}]->(c)

*********************************************************************/
     public void insertarProducto(String codigoProducto, String descripcionProducto,String categoria,String marca){
     /*     String consultaCypher="MATCH (m:marca) where m.nombre='$marca' "+
          "MATCH (c:categoria) where c.nombre='$categoria' "+
          "CREATE (p:producto{codigo:'$codigoProducto', descripcion:'$descripcionProducto'}) "+
          "CREATE(p)-[r1:ES{}]->(m) "+
          "CREATE(p)-[r2:PERTENECE{}]->(c)";
          System.out.println(consultaCypher);*/
          String consultaCypher2="MATCH (m:marca) where m.nombre='"+marca+"' "+
          "MATCH (c:categoria) where c.nombre='"+categoria+"' "+
          "CREATE (p:producto{codigo:'"+codigoProducto+"', descripcion:'"+descripcionProducto+"'}) "+
          "CREATE(p)-[r1:ES{}]->(m) "+
          "CREATE(p)-[r2:PERTENECE{}]->(c)";
           System.out.println(consultaCypher2);
          Session sesion = driver.session();
      //  Result resultado=sesion.run(consultaCypher,parameters("marca",marca,"categoria",categoria,"codigoProducto",codigoProducto,"descripcionProducto",descripcionProducto));
          Result resultado2=sesion.run(consultaCypher2);
          sesion.close();


      }

     
     public void insertarCategoria(String nombre){
        Session sesion = driver.session();
        String strTrans;
        strTrans = sesion.writeTransaction(new TransactionWork<String>(){
            @Override
            public String execute (Transaction tx) {
                Result resultado = (Result) tx.run("CREATE(c:categoria {nombre:$nombre})", parameters ("nombre",nombre));
                return "";
                        }
        });
          
        sesion.close();
    }
     
       public void insertarTienda(String nombre, String email, String TLF, String localidad, String direccion){
        Session sesion = driver.session();
        String strTrans;
        strTrans = sesion.writeTransaction(new TransactionWork<String>(){
            @Override
            public String execute (Transaction tx) {
                Result resultado = (Result) tx.run("CREATE(t:tienda {nombre:$nombre, email:$email, TLF:$TLF, localidad:$localidad, direccion:$direccion})", parameters ("nombre",nombre, "email",email, "TLF",TLF, "localidad",localidad, "direccion",direccion));
                return "";
                        }
        });
          
        sesion.close();
    }
       public void insertarMarca(String nombre){
        Session sesion = driver.session();
        String strTrans;
        strTrans = sesion.writeTransaction(new TransactionWork<String>(){
            @Override
            public String execute (Transaction tx) {
                Result resultado = (Result) tx.run("CREATE(m:marca {nombre:$nombre})", parameters ("nombre",nombre));
                return "";
                        }
        });
          
        sesion.close();
    }
        public void relacionarProductoTienda(String nombreTienda, String codProducto, double precio,int stock){
        Session sesion = driver.session();
        String consulta="MATCH (t:tienda) where t.nombre='"+nombreTienda+"' "+
                "MATCH (p:producto) where p.codigo='"+codProducto+"' "+
                "CREATE (p)-[r:DISPONIBLE{precio:"+precio+",stock:"+stock+"}]->(t)";
            System.out.println(consulta);
        Result resultado=sesion.run(consulta);
        sesion.close();
    }
       
        public void ejecutar(String sentenciaCypher){
            try{
            Session sesion = driver.session();
            String strTrans = sesion.writeTransaction(new TransactionWork<String>(){
                @Override
                public String execute(Transaction tx){
                    Result resultado = (Result)tx.run(sentenciaCypher);
                    return"";
                }
            });
            sesion.close();
            }catch(Exception e){
                System.out.println("Error en la consulta");
            }
        }
               public void eliminarTienda(String nombre){
        Session sesion = driver.session();
        String strTrans;
        strTrans = sesion.writeTransaction(new TransactionWork<String>(){
            @Override
            public String execute (Transaction tx) {
                Result resultado = (Result) tx.run("MATCH(t:tienda) WHERE t.nombre='" + nombre + "' DELETE t");
                return "";
                        }
            });
            sesion.close();
        }
               public void eliminarProducto(String codigo){
        Session sesion = driver.session();
        String strTrans;
        strTrans = sesion.writeTransaction(new TransactionWork<String>(){
            @Override
            public String execute (Transaction tx) {
                Result resultado = (Result) tx.run("MATCH(p:producto) WHERE p.codigo='" + codigo + "' DELETE p");
                return "";
                        }
            });
            sesion.close();                              
        }   
               public void eliminarCategoria(String nombre){
        Session sesion = driver.session();
        String strTrans;
        strTrans = sesion.writeTransaction(new TransactionWork<String>(){
            @Override
            public String execute (Transaction tx) {
                Result resultado = (Result) tx.run("MATCH(c:categoria) WHERE c.nombre='" + nombre + "' DELETE c");
                return "";
                        }
            });
            sesion.close();              
        }
           public void eliminarMarca(String nombre){
        Session sesion = driver.session();
        String strTrans;
        strTrans = sesion.writeTransaction(new TransactionWork<String>(){
            @Override
            public String execute (Transaction tx) {
                Result resultado = (Result) tx.run("MATCH(m:marca) WHERE m.nombre='" + nombre + "' DELETE m");
                return "";
                        }
            });
            sesion.close();
        }
          public void eliminarTodo(){
        Session sesion = driver.session();
        String strTrans;
        strTrans = sesion.writeTransaction(new TransactionWork<String>(){
            @Override
            public String execute (Transaction tx) {
                Result resultado = (Result) tx.run("MATCH(r) DETACH DELETE r");
                return "";
                        }
            });
            sesion.close();
        } 
          public void eliminarTodasLasRelacionesPorNombre(String tipoNodo, String nombreRelacion){
              Session sesion = driver.session();
              String sentenciaCypher="match (p1:"+tipoNodo+")-[r:"+nombreRelacion+"]-(p2"+tipoNodo+") delete r";
              System.out.println(sentenciaCypher);
              Result resultado=sesion.run(sentenciaCypher);
        }
          public void eliminarRelacionYNodos(String tipoNodo, String nombreRelacion){
              Session sesion = driver.session();
              String sentenciaCypher = "match (p1:"+tipoNodo+")-[r:"+nombreRelacion+"]-(p2:"+tipoNodo+") delete p1, p2, r";      
              System.out.println(sentenciaCypher);
              Result resultado=sesion.run (sentenciaCypher);
        }
          public boolean eliminarNodo(String tipoNodo,String condicion){
              Session sesion = driver.session();
              String sentenciaCypher="MATCH (nodo:"+tipoNodo+") WHERE nodo."+condicion+" DETACH DELETE nodo RETURN nodo";
              System.out.println(sentenciaCypher);
              Result resultado=sesion.run(sentenciaCypher);
       
                 if(resultado !=null){
                     System.out.println("Nodo eliminado");
                     return true;
            
             }
                 System.out.println("Nodo NO eliminado");
              return false;
    }
          
          
          
       public ArrayList<Record> obtenerTipos(){
        Session sesion = driver.session();
        ArrayList<Record> lista=new ArrayList<>();
        Result resultado=sesion.run("MATCH (cat:categoria) return cat.nombre,cat.descripcion");
        while(resultado.hasNext()){
            lista.add(resultado.next());
        }
       
        return lista;
       }
       
        public ArrayList<Record> obtenerTiendas(){
        Session sesion = driver.session();
        ArrayList<Record> lista=new ArrayList<>();
        Result resultado=sesion.run("MATCH (t:tienda) return t.nombre");
        while(resultado.hasNext()){
            lista.add(resultado.next());
        }
       
        return lista;
       }
       public ArrayList<Record> obtenerMarcas(){
        Session sesion = driver.session();
        ArrayList<Record> lista=new ArrayList<>();
        Result resultado=sesion.run("MATCH (mar:marca) return mar.nombre");
        while(resultado.hasNext()){
            lista.add(resultado.next());
        }
       
        return lista;
       }
       
       public ArrayList<Record> obtenerProductos(){
        Session sesion = driver.session();
        ArrayList<Record> lista=new ArrayList<>();
        Result resultado=sesion.run("MATCH (p:producto) return p.codigo,p.descripcion");
        while(resultado.hasNext()){
            lista.add(resultado.next());
        }
       
        return lista;
       }
       
       /*Quita comillas al principio y al final*/
       public static String limpiar(String texto){
           String sinComillas="";
           for (int i = 0; i < texto.length(); i++) {
               if(texto.charAt(i)!='"'){
                    sinComillas+=texto.charAt(i);
               }
              
           }
           return sinComillas;
       }
       
       public boolean existeNodo(String codigoProducto){
            Session sesion = driver.session();
        ArrayList<Record> lista=new ArrayList<>();
       // Result resultado=sesion.run("MATCH (p:producto) where p.codigo='$codigo' return count(p)",parameters("codigo",codigoProducto));
       Result resultado=sesion.run("MATCH (p:producto) where p.codigo='"+codigoProducto+"' return count(p)");
        int total= Integer.parseInt(resultado.next().get(0).toString());
        System.out.println(total);
        if (total>0){
            return true;
        }
        return false;
           
       }
       public void crearCopiaSeguridad(){
            Session sesion = driver.session();
                 String strTrans;
                
        strTrans = sesion.writeTransaction(new TransactionWork<String>(){
            @Override
            public String execute (Transaction tx) {
                 File f=new File(".");
                System.out.println(f.getPath());
                Result resultado = (Result) tx.run ("CALL apoc.export.csv.all('tienda.csv', {})");
                return "";
                        }
            });
            sesion.close();           
       }
       
        public void cargarCopiaSeguridad(){
            Session sesion = driver.session();
                 String strTrans;
        strTrans = sesion.writeTransaction(new TransactionWork<String>(){
            @Override
            public String execute (Transaction tx) {
                File f=new File(".");
                
                Result resultado = (Result) tx.run ("CALL apoc.import.csv('tienda.csv', {})");
                return "";
                        }
            });
            sesion.close();           
       }
       
       public ArrayList<Record> obtenerDatos(String sentencia){
        Session sesion = driver.session();
        ArrayList<Record> lista=new ArrayList<>();
        Result resultado=sesion.run(sentencia);
        while(resultado.hasNext()){
            lista.add(resultado.next());
        }
       
        return lista;
       }
        public ArrayList<Record> obtenerRoles(){
        Session sesion = driver.session();
        ArrayList<Record> lista=new ArrayList<>();
        Result resultado=sesion.run("MATCH (u:usuario) return u.rol, u.pass");
        while(resultado.hasNext()){
            lista.add(resultado.next());
        }
       
        return lista;
       }
       
       
        
}

