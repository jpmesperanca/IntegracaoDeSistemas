package servlet;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DTOs.GregorianCalendarDTO;
import DTOs.TicketInfoDTO;
import DTOs.TripInfoDTO;
import DTOs.UserInfoDTO;
import beans.StatelessBean;
import data.Passenger;

@WebServlet("/webaccess")
public class TestServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@EJB
	private StatelessBean slb;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		slb.createData();

		response.getWriter().print("Dados criados!\n");

		List<Integer> field1List = slb.listPassengers().stream().map(Passenger::getId).collect(Collectors.toList());
		String result = "Passenger list: " + field1List + "\n";
		System.out.println(result);
		response.getWriter().print(result);

		// -------------------- 6 --------------------
		// As a user, I want to edit my personal information.
		response.getWriter().print("-------------------- 6 --------------------\n");
		response.getWriter().print("\"As a user, I want to edit my personal information.\"\n\n");

		// ir buscar dados iniciais deste user
		UserInfoDTO uInfo = slb.getPassengerInfoById(field1List.get(0));

		response.getWriter().print("Passenger ID  [" + field1List.get(0) + "].\nName: " + uInfo.getName() + "\nEmail: "
				+ uInfo.getEmail() + "\nPhone Number: " + uInfo.getPhoneNumber() + "\n\n");

		// alterar o mail e nome

		// ALTEREI ISTO PQ N TAVA A USAR DTO
		// slb.editPassenger(field1List.get(0), "novo@email.com", null, "Jose II",
		// null);

		// ir buscar a info outra vez para ver se mudou
		uInfo = slb.getPassengerInfoById(field1List.get(0));
		response.getWriter().print("Did it work?\nPassenger ID  [" + field1List.get(0) + "].\nName: " + uInfo.getName()
				+ "\nEmail: " + uInfo.getEmail() + "\nPhone Number: " + uInfo.getPhoneNumber() + "\n\n");

		// -------------------- 7 --------------------
		// As a user, I want to delete my account, thus erasing all traces of my
		// existence fromthe system, including my available items.

		response.getWriter().print("-------------------- 7 --------------------\n");
		response.getWriter().print(
				"\"As a user, I want to delete my account, thus erasing all traces of my existence from the system, including my available items.\"\n\n");

		// mostrar a atual lista de passengers
		response.getWriter().print("Passenger list: " + field1List + "\n");

		// apagar o primeiro passenger
		slb.deletePassenger(field1List.get(0));
		response.getWriter().print("Deleted user with ID = " + field1List.get(0) + "\n");

		// voltar a ir buscar e mostrar a lista de passengers
		field1List = slb.listPassengers().stream().map(Passenger::getId).collect(Collectors.toList());
		response.getWriter().print("Updated Passenger list: " + field1List + "\n\n");

		// -------------------- 8 --------------------
		// As a user, I want to list the available trips, providing date intervals.
		response.getWriter().print("-------------------- 8 --------------------\n");
		response.getWriter().print("\"As a user, I want to list the available trips, providing date intervals.\"\n\n");

		response.getWriter().print("Listing Available Trips between 1999 and 2001");
		List<TripInfoDTO> availableTrips = slb.listTripInfoBetweenStartEndDate(new GregorianCalendarDTO(1999, 0, 1),
				new GregorianCalendarDTO(2001, 0, 1));

		for (TripInfoDTO t : availableTrips) {
			response.getWriter().print("\n----------\n");
			int m = t.getDepartureDate().getMonth() + 1;
			response.getWriter()
					.print("Trip ID [" + t.getTripId() + "]\nDeparture Date: " + t.getDepartureDate().getDay() + "/" + m
							+ "/" + t.getDepartureDate().getYear() + "\nDeparture Point: " + t.getDeparturePoint()
							+ "\nDestination Point: " + t.getDestinationPoint() + "\nCapacity: " + t.getCapacity()
							+ "px\nTicket Price: " + t.getTicketPrice() + " EUR");
		}
		response.getWriter().print("\n----------\n\n");

		// -------------------- 9 --------------------
		// As a user, I want to charge my wallet to be able to purchase tickets. (You
		// may ignore the step that involves the payment itself).
		response.getWriter().print("-------------------- 9 --------------------\n");
		response.getWriter().print(
				"\"As a user, I want to charge my wallet to be able to purchase tickets. (You may ignore the step that involves the payment itself)\"\n\n");

		// Mostrar balance de um passageiro
		uInfo = slb.getPassengerInfoById(field1List.get(0));
		response.getWriter().print("Passenger ID = " + field1List.get(0) + "\nBalance: " + uInfo.getBalance()
				+ " EUR\nCharging wallet with 20.0 EUR\n");
		// Carregar a wallet
		slb.chargeWallet(field1List.get(0), 20.0);

		// Ir buscar a info outra vez
		uInfo = slb.getPassengerInfoById(field1List.get(0));
		response.getWriter().print("New Balance: " + uInfo.getBalance() + " EUR\n\n");

		// -------------------- 10 --------------------
		// As a user, I want to purchase a ticket. I should be able to select the place.
		response.getWriter().print("-------------------- 10 --------------------\n");
		response.getWriter()
				.print("\"As a user, I want to purchase a ticket. I should be able to select the place.\"\n\n");

		// Ir buscar a lista atual de tickets deste passenger
		response.getWriter().print(">> I have " + uInfo.getBalance() + " EUR and these are my tickets right now:\n");
		List<TicketInfoDTO> passengerTickets = slb.listTicketsByPassengerId(field1List.get(0));
		TripInfoDTO trip = new TripInfoDTO();
		for (TicketInfoDTO t : passengerTickets) {
			trip = slb.getTripInfo(t.getTripId());
			response.getWriter().print(trip.toString());
			response.getWriter().print("----------\n");
		}

		response.getWriter().print("\n>> Purchasing ticket for Trip [" + availableTrips.get(0).getTripId() + "]\n");

		slb.purchaseTicket(field1List.get(0), availableTrips.get(0).getTripId());

		// Ir buscar a lista atualizada de tickets deste passenger e o balance
		uInfo = slb.getPassengerInfoById(field1List.get(0));
		response.getWriter()
				.print(">> I now have " + uInfo.getBalance() + " EUR and these are my trips after the purchase:\n");

		passengerTickets = slb.listTicketsByPassengerId(field1List.get(0));
		for (TicketInfoDTO t : passengerTickets) {
			trip = slb.getTripInfo(t.getTripId());
			response.getWriter().print(trip.toString());
			response.getWriter().print("----------\n");
		}

		// -------------------- 11 --------------------
		// As a user, I may be able to return a ticket for future trips and get a
		// reimbursement
		response.getWriter().print("\n-------------------- 11 --------------------\n");
		response.getWriter()
				.print("\"As a user, I may be able to return a ticket for future trips and get a reimbursement.\"\n\n");

		// mostrar viagens que tem atualmente
		response.getWriter().print(">> I have " + uInfo.getBalance() + " EUR and these are my trips:\n");
		// passengerTickets = slb.listTicketsByPassengerId(field1List.get(0));
		response.getWriter().print("----------\n");
		for (TicketInfoDTO t : passengerTickets) {
			trip = slb.getTripInfo(t.getTripId());
			response.getWriter().print(trip.toString());
			response.getWriter().print("----------\n");
		}
		// dar refund à primeira
		response.getWriter().print("\n>> Refunding Ticket [" + passengerTickets.get(0).getTicketId() + "] for Trip ["
				+ passengerTickets.get(0).getTripId() + "]\n");

		slb.refundTicket(field1List.get(0), passengerTickets.get(0).getTicketId());

		// Ir buscar a lista atualizada de viagens deste passenger e o balance
		uInfo = slb.getPassengerInfoById(field1List.get(0));
		response.getWriter()
				.print(">> I now have " + uInfo.getBalance() + " EUR and these are my trips after the refund:\n");
		response.getWriter().print("----------\n");
		passengerTickets = slb.listTicketsByPassengerId(field1List.get(0));
		for (TicketInfoDTO t : passengerTickets) {
			trip = slb.getTripInfo(t.getTripId());
			response.getWriter().print(trip.toString());
			response.getWriter().print("----------\n");
		}

		// -------------------- 12 --------------------
		// As a user, I can list my trips.
		response.getWriter().print("\n-------------------- 12 --------------------\n");
		response.getWriter().print("\"As a user, I can list my trips.\"\n\n");

		// mostrar viagens que tem atualmente
		response.getWriter().print(">> These are my trips:\n");
		// ir buscar lista de tickets
		passengerTickets = slb.listTicketsByPassengerId(field1List.get(0));
		response.getWriter().print("----------\n");
		for (TicketInfoDTO t : passengerTickets) {
			trip = slb.getTripInfo(t.getTripId());
			response.getWriter().print(trip.toString());
			response.getWriter().print("----------\n");
		}

		// -------------------- 13 --------------------
		// As a company manager, I want to create future bus trips, including the
		// departure date and hour, departure point, destination, capacity, and price.
		// You may assume that departure and destination points are limited.
		response.getWriter().print("\n-------------------- 13 --------------------\n");
		response.getWriter().print(
				"\"As a company manager, I want to create future bus trips, including the departure date and hour, departure point, destination, capacity, and price. You may assume that departure and destination points are limited.\"\n\n");

		// -------------------- 14 --------------------
		// As a company manager, I want to delete future bus trips. The money of all
		// tickets should be returned to the correct wallets, and the system should warn
		// the affected users by email.
		response.getWriter().print("\n-------------------- 14 --------------------\n");
		response.getWriter().print(
				"\"As a company manager, I want to delete future bus trips. The money of all tickets should be returned to the correct wallets, and the system should warn the affected users by email.\"\n\n");

		response.getWriter().print(">> These are all the available trips: \n");

		availableTrips = slb.listTripInfoBetweenStartEndDate(new GregorianCalendarDTO(1990, 0, 1),
				new GregorianCalendarDTO(2022, 0, 1));

		for (TripInfoDTO t : availableTrips) {
			response.getWriter().print("\n----------\n");
			response.getWriter().print(t.toString());
		}
		response.getWriter().print("\n----------\n");

		// mostrar viagens dos utilizadores
		// comprar a viagem do gajo anterior so para uma viagem ter varios passageiros
		slb.purchaseTicket(field1List.get(0), availableTrips.get(2).getTripId());
		uInfo = slb.getPassengerInfoById(field1List.get(0));
		response.getWriter().print(">> I'm user [" + field1List.get(0) + "] I have " + uInfo.getBalance()
				+ " EUR and these are my trips:\n");
		// ir buscar lista de tickets
		passengerTickets = slb.listTicketsByPassengerId(field1List.get(0));
		response.getWriter().print("----------\n");
		for (TicketInfoDTO t : passengerTickets) {
			trip = slb.getTripInfo(t.getTripId());
			response.getWriter().print(trip.toString());
			response.getWriter().print("----------\n");
		}

		uInfo = slb.getPassengerInfoById(field1List.get(1));
		response.getWriter().print(">> I'm user [" + field1List.get(1) + "] I have " + uInfo.getBalance()
				+ " EUR and these are my trips:\n"); // ir buscar lista de tickets
		passengerTickets = slb.listTicketsByPassengerId(field1List.get(1));
		response.getWriter().print("----------\n");
		for (TicketInfoDTO t : passengerTickets) {
			trip = slb.getTripInfo(t.getTripId());
			response.getWriter().print(trip.toString());
			response.getWriter().print("----------\n");
		}

		// delete one trip
		response.getWriter().print(
				"\n>> I'm a manager and I'm deleting Trip with ID [" + availableTrips.get(2).getTripId() + "]\n");
		slb.deleteTrip(availableTrips.get(2).getTripId());

		// ---------------------------------------------------------------------------------------------------------------------------------------
		response.getWriter().print("\n>> PRINTAR TUDO AGAIN A VER SE FUNCIONOU :D\n");

		response.getWriter().print(">> These are all the available trips: \n");

		availableTrips = slb.listTripInfoBetweenStartEndDate(new GregorianCalendarDTO(1990, 0, 1),
				new GregorianCalendarDTO(2022, 0, 1));

		for (TripInfoDTO t : availableTrips) {
			response.getWriter().print("\n----------\n");
			response.getWriter().print(t.toString());
		}
		response.getWriter().print("\n----------\n");

		// mostrar viagens dos utilizadores
		// comprar a viagem do gajo anterior so para uma viagem ter varios passageiros
		uInfo = slb.getPassengerInfoById(field1List.get(0));
		response.getWriter().print(">> I'm user [" + field1List.get(0) + "] I have " + uInfo.getBalance()
				+ " EUR and these are my trips:\n");
		// ir buscar lista de tickets
		passengerTickets = slb.listTicketsByPassengerId(field1List.get(0));
		response.getWriter().print("----------\n");
		for (TicketInfoDTO t : passengerTickets) {
			trip = slb.getTripInfo(t.getTripId());
			response.getWriter().print(trip.toString());
			response.getWriter().print("----------\n");
		}

		uInfo = slb.getPassengerInfoById(field1List.get(1));
		response.getWriter().print(">> I'm user [" + field1List.get(1) + "] I have " + uInfo.getBalance()
				+ " EUR and these are my trips:\n"); // ir buscar lista de tickets
		passengerTickets = slb.listTicketsByPassengerId(field1List.get(1));
		response.getWriter().print("----------\n");
		for (TicketInfoDTO t : passengerTickets) {
			trip = slb.getTripInfo(t.getTripId());
			response.getWriter().print(trip.toString());
			response.getWriter().print("----------\n");
		}

		// -------------------- 15 --------------------
		// As a company manager I want to list the passengers that have made more trips
		// (e.g., the top 5).
		response.getWriter().print("\n-------------------- 15 --------------------\n");
		response.getWriter().print(
				"\"As a company manager I want to list the passengers that have made more trips (e.g., the top 5).\"\n\n");

		List<Integer> top5 = slb.listTop5Passengers();
		response.getWriter().print("----------\n");
		for (Integer pId : top5) {
			response.getWriter().print(slb.getPassengerInfoById(pId).toString());
			response.getWriter().print("\n----------\n");
		}

		// -------------------- 16 --------------------
		// As a company manager I want to search for all bus trips sorted by date
		// between two date limits.
		response.getWriter().print("\n-------------------- 16 --------------------\n");
		response.getWriter().print(
				"\"As a company manager I want to search for all bus trips sorted by date between two date limits.\"\n\n");

		response.getWriter().print(">> These are all the available trips: \n");

		availableTrips = slb.listTripInfoBetweenStartEndDate(new GregorianCalendarDTO(1990, 0, 1),
				new GregorianCalendarDTO(2022, 0, 1));

		for (TripInfoDTO t : availableTrips) {
			response.getWriter().print("\n----------\n");
			response.getWriter().print(t.toString());
		}
		response.getWriter().print("\n----------\n");

		// -------------------- 17 --------------------
		// As a company manager I want to search for all bus trips occurring on a given
		// date.
		response.getWriter().print("\n-------------------- 17 --------------------\n");
		response.getWriter()
				.print("\"As a company manager I want to search for all bus trips occurring on a given date.\"\n\n");

		availableTrips = slb.listTripInfoByDepartureDate(new GregorianCalendarDTO(1995, 5, 15));

		for (TripInfoDTO t : availableTrips) {
			response.getWriter().print("\n----------\n");
			response.getWriter().print(t.toString());
		}
		response.getWriter().print("\n----------\n");

		// -------------------- 18 --------------------
		// As a company manager I want to list all passengers on a given trip listed
		// during one of the previous searches.
		response.getWriter().print("\n-------------------- 18 --------------------\n");
		response.getWriter().print(
				"\"As a company manager I want to list all passengers on a given trip listed during one of the previous searches.\"\n\n");

		availableTrips = slb.listTripInfoBetweenStartEndDate(new GregorianCalendarDTO(1990, 0, 1),
				new GregorianCalendarDTO(2022, 0, 1));
		List<Integer> passengers = slb.listPassengersByTripId(availableTrips.get(1).getTripId());

		for (Integer pId : passengers) {
			response.getWriter().print("\n----------\n");
			response.getWriter().print(slb.getPassengerInfoById(pId).toString());
		}
		response.getWriter().print("----------\n");

		// fim
		slb.eraseAllData();
		response.getWriter().print("\n\nDados apagados!\n");

	}

}