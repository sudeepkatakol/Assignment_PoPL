public enum Kind {
	Int, Float, Bool, Double, Void;

	public static Kind getKind(String kind) throws Exception{
		switch (kind) {
			case "void": return Kind.Void;
			case "int": return Kind.Int;
			case "double": return Kind.Double;
			case "bool": return Kind.Bool;
			case "float": return Kind.Float;
			default: throw new Exception("Invalid kind.");
		}
	}
}