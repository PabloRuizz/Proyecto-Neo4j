function dibujar(){
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
            }
			
			},
        relationships:{
			"PERTENECE":{
				caption: true,
				thickness:"count"
			}
        },
        initial_cypher: "MATCH (p:producto) match(t:tienda) return p,t"

    }
	var viz = new NeoVis.default(config);
	viz.render();
}