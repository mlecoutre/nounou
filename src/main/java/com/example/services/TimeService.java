package com.example.services;

import com.example.models.Time;
import java.util.TimeZone;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/time")
@Produces("application/json;charset=UTF-8")
public class TimeService {

	@GET
	public Time get() {
		return new Time();
	}

	@GET
	@Path("/{timezone}")
	public Time get(@PathParam("timezone") String timezone) {
		return new Time(TimeZone.getTimeZone(timezone.toUpperCase()));
	}

	@GET
	@Path("/{hello}")
	public String hello(@PathParam("name") String name) {
		return "Hello, World " + name;
	}
}
