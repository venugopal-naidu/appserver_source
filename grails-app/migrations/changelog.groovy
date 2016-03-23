databaseChangeLog = {

	changeSet(author: "roopesh", id: "ddl-1") {
		sqlFile( path: "ddl.sql")
	}

	changeSet(author: "roopesh", id: "dml-1") {
		sqlFile( path: "dml.sql")
	}

}
