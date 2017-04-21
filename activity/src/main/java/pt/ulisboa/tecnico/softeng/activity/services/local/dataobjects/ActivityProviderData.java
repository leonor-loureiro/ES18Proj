package pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects;

import java.util.List;
import java.util.stream.Collectors;

import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;

public class ActivityProviderData {
	private String code;
	private String name;
	private List<ActivityData> activities;

	public ActivityProviderData() {
	}

	public ActivityProviderData(ActivityProvider provider) {
		this.code = provider.getCode();
		this.name = provider.getName();
		this.setActivities(provider.getActivitySet().stream().sorted((a1, a2) -> a1.getName().compareTo(a2.getName()))
				.map(a -> new ActivityData(a)).collect(Collectors.toList()));
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ActivityData> getActivities() {
		return activities;
	}

	public void setActivities(List<ActivityData> activities) {
		this.activities = activities;
	}

}
