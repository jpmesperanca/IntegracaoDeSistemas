package book;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import data.Client;
import data.Currency;
import data.Manager;

@RequestScoped
@Path("/myservice")
@Produces(MediaType.APPLICATION_JSON)
public class MyService {

    @PersistenceContext(name = "proj3", type = javax.persistence.PersistenceContextType.EXTENDED)
    EntityManager em;

    @GET
    @Path("/listClients")
    public List<Client> listClients() {

        TypedQuery<Client> q = em.createQuery("from Client c", Client.class);

        List<Client> l = q.getResultList();

        return l;
    }

    @GET
    @Path("/listManagers")
    public List<Manager> listManagers() {

        TypedQuery<Manager> q = em.createQuery("from Manager m", Manager.class);

        List<Manager> l = q.getResultList();

        return l;
    }

    @GET
    @Path("/listCurrency")
    public List<Currency> listCurrency() {

        TypedQuery<Currency> q = em.createQuery("from Currency c", Currency.class);

        List<Currency> l = q.getResultList();

        return l;
    }

    @POST
    @Transactional
    @Path("/addManager")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addManager(Manager m) {

        // TODO - Add protections de parametros nulos

        em.persist(m);

        return Response.ok().entity(m).build();
    }

    @POST
    @Transactional
    @Path("/addClient")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addClient(Client c) {

        // TODO - Add protections de parametros nulos

        em.persist(c);

        return Response.ok().entity(c).build();
    }

    @POST
    @Transactional
    @Path("/addCurrency")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCurrency(Currency c) {

        // TODO - Add protections de parametros nulos

        em.persist(c);

        return Response.ok().entity(c).build();
    }
}
