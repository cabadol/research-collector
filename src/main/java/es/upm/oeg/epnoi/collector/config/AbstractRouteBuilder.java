package es.upm.oeg.epnoi.collector.config;


import es.upm.oeg.epnoi.collector.CollectorRouteBuilder;
import es.upm.oeg.epnoi.collector.Header;
import es.upm.oeg.epnoi.collector.model.Provider;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.RouteDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractRouteBuilder implements IRouteBuilder{

    private static final Logger LOG = LoggerFactory.getLogger(AbstractRouteBuilder.class);

    public static final String XPATH = "$";

    private final Namespaces ns;

    public AbstractRouteBuilder(){
        this.ns = new Namespaces("oai", "http://www.openarchives.org/OAI/2.0/")
                .add("dc", "http://purl.org/dc/elements/1.1/")
                .add("provenance", "http://www.openarchives.org/OAI/2.0/provenance")
                .add("oai_dc", "http://www.openarchives.org/OAI/2.0/oai_dc/")
                .add("rss","http://purl.org/rss/1.0/");
    }


    protected void addProperties(RouteBuilder builder, ProcessorDefinition<RouteDefinition> def, Provider provider){
        add(Header.SOURCE_NAME, provider.getName(), def, builder);
        add(Header.SOURCE_URI, provider.getUri(), def, builder);
        add(Header.SOURCE_URL, provider.getUrl(), def, builder);
        add(Header.SOURCE_PROTOCOL, provider.getProtocol(), def, builder);
        add(Header.PUBLICATION_TITLE, provider.getPublication().getTitle(), def, builder);
        add(Header.PUBLICATION_DESCRIPTION, provider.getPublication().getDescription(), def, builder);
        add(Header.PUBLICATION_PUBLISHED, provider.getPublication().getPublished(), def, builder);
        add(Header.PUBLICATION_URI, provider.getPublication().getUri(), def, builder);
        add(Header.PUBLICATION_URL_REMOTE, provider.getPublication().getUrl(), def, builder);
        add(Header.PUBLICATION_LANGUAGE, provider.getPublication().getLanguage(), def, builder);
        add(Header.PUBLICATION_RIGHTS, provider.getPublication().getRights(), def, builder);
        add(Header.PUBLICATION_CREATORS, provider.getPublication().getCreators(), def, builder);
        add(Header.PUBLICATION_FORMAT, provider.getPublication().getFormat(), def, builder);
        add(Header.PUBLICATION_REFERENCE_FORMAT, "xml", def, builder);
        def.to(CollectorRouteBuilder.INBOX_ROUTE);
        LOG.debug("Route definition: {}", def);
    }

    private void add(String property, String expression, ProcessorDefinition<RouteDefinition> def, RouteBuilder builder ){
        if (expression.startsWith(XPATH)){
            def.setProperty(property, builder.xpath(expression.replace(XPATH,""),String.class).namespaces(ns));
        }else{
            def.setProperty(property,builder.constant(expression));
        }
    }
}