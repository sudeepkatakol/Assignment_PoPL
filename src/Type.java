public enum Type {
	fun, var, par;

	public static Type getType(String type) throws Exception{
		switch (type) {
			case "par": return Type.par;
			case "fun": return Type.fun;
			case "var": return Type.var;
			default: throw new Exception("Invalid type.");
		}
	}

}