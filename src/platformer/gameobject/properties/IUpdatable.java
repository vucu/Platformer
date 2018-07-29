package platformer.gameobject.properties;

public interface IUpdatable {
	public void onUpdateBegin();

	public void onUpdate();

	public void onUpdateEnd();
}
