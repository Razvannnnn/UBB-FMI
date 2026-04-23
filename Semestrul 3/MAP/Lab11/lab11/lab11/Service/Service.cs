using lab11.Domain;
using lab11.Repository;
using lab11.Repository.File;

namespace lab11.Service;

public class Service
{
    private Repository<Guid, Echipa> echipe;
    private Repository<Guid, Jucator> jucatori;
    private Repository<Guid, Meci> meciuri;
    private Repository<Guid, Elev> elevi;
    private Repository<Guid, JucatorActiv> jucatoriActivi;
    
    public Service(Repository<Guid, Echipa> echipe, Repository<Guid, Jucator> jucatori, Repository<Guid, Meci> meciuri, Repository<Guid, Elev> elevi, Repository<Guid, JucatorActiv> jucatoriActivi)
    {
        this.echipe = echipe;
        this.jucatori = jucatori;
        this.meciuri = meciuri;
        this.elevi = elevi;
        this.jucatoriActivi = jucatoriActivi;
    }

    public IEnumerable<Jucator> JucatoriAiUneiEchipe(Echipa echipa)
    {
        return jucatori.findAll().Where(x =>
        {
            return x.echipa.Equals(echipa);
        });
    }

    public IEnumerable<Jucator> JucatoriActiviLaUnMeciAiUneiEchipe(Echipa echipa, Meci meci)
    {
        return
            from jucatorActiv in jucatoriActivi.findAll()
            join jucator in jucatori.findAll() on jucatorActiv.idJucator equals jucator.id
            where jucatorActiv.idMeci == meci.id && jucator.echipa.id == echipa.id
            select jucator;
    }
    
    public IEnumerable<Meci> MeciuriDintrOPerioada(DateTime begin, DateTime end)
    {
        return
            from meci in meciuri.findAll()
            where meci.data >= begin && meci.data <= end
            select meci;
    }

    public string ScorLaUnMeci(Meci meci)
    {
        int scorEchipa1 = 0;
        int scorEchipa2 = 0;
        try
        {
            scorEchipa1 = (
                from jucatorActiv in jucatoriActivi.findAll()
                where jucatori.findOne(jucatorActiv.idJucator).echipa.id == meci.echipa1.id && jucatorActiv.idMeci == meci.id
                select jucatorActiv
            ).Sum(x => x.nrPuncteInscrise);
        } catch (Exception e) { }
        
        try
        {
            scorEchipa2 = (
                from jucatorActiv in jucatoriActivi.findAll()
                where jucatori.findOne(jucatorActiv.idJucator).echipa.id == meci.echipa2.id && jucatorActiv.idMeci == meci.id
                select jucatorActiv
            ).Sum(x => x.nrPuncteInscrise);
        } catch (Exception e) { }
        
        return scorEchipa1.ToString() + " - " + scorEchipa2.ToString();
    }
    
    public Echipa findEchipa(Guid id)
    {
        return echipe.findOne(id);
    }
    
    public Meci findMeci(Guid id)
    {
        return meciuri.findOne(id);
    }
    
}