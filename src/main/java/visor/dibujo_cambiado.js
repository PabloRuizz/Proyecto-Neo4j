function dibujar(cypher){
	var sentencia=cypher.replace("%20"," ");
	contador=0;
	posicion=sentencia.indexOf("%20");
	while(posicion!=-1){
		contador++;
		sentencia=sentencia.replace("%20"," ");
		posicion=sentencia.indexOf("%20",posicion+1);
	}
	alert(sentencia);
    var config={
        container_id: "viz",
        server_url:"bolt://localhost:7687",
        server_user:"neo4j",
        server_password:"123",
        labels:{
            "producto":{
                caption:"descripcion",
                size:"pagerank",
                community:"community"
            },
			"tienda":{
                caption:"nombre",
                size:"pagerank",
                community:"community"
            }
			
			},
        relationships:{
			"PERTENECE":{
				caption: true,
				thickness:"count"
			}
        },
        initial_cypher: sentencia
    }
	
	var viz = new NeoVis.default(config);
	viz.render();
}
 function getQueryVariable(variable) {
        var query = window.location.search.substring(1);
        var vars = query.split("&");
        for (var i = 0; i < vars.length; i++) {
            var pair = vars[i].split("=");
               if (pair[0] == variable) {
                return pair[1];
            }
        }
        return false;
    }