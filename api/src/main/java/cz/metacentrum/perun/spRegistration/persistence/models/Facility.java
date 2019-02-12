package cz.metacentrum.perun.spRegistration.persistence.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Representation of Perun Facility.
 *
 * @author Dominik Frantisek Bucik <bucik@ics.muni.cz>
 */
public class Facility extends PerunEntity {

	private String name;
	private String description;
	private Map<String, PerunAttribute> attrs = new HashMap<>();

	public Facility(Long id) {
		super(id);
	}

	public Facility(Long id, String name, String description) {
		super(id);
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, PerunAttribute> getAttrs() {
		return attrs;
	}

	public void setAttrs(Map<String, PerunAttribute> attrs) {
		this.attrs = attrs;
	}

	/**
	 * Convert object to JSON representation
	 * @return JSON String
	 */
	public String toJsonString() {
		ObjectMapper mapper = new ObjectMapper();
		String json = null;
		try {
			json = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{ \"facility\" : " + json + " }";
	}

	public static Facility fromPerunJson(JSONObject json) {
		Long id = json.getLong("id");
		String name = json.getString("name");
		String description = json.optString("description", "");

		return new Facility(id, name, description);
	}

	@Override
	public boolean equals(Object o) {
		if (! (o instanceof Facility)) {
			return false;
		}

		Facility them = (Facility) o;
		return Objects.equals(this.getId(), them.getId())
				&& Objects.equals(this.name, them.name);
	}

	@Override
	public String toString() {
		return "Facility{" +
				super.toString() +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", attrs=" + attrs +
				'}';
	}
}