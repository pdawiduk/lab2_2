import org.junit.Test;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.invoicing.*;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Shogun on 2016-06-16.
 */
public class InvoiceTest {

    @Test
    public void NetAndGrosCalculateTest() {

        ClientData client = new ClientData(Id.generate(), "New Client");
        Invoice invoice = new InvoiceFactory().create(client);
        TaxPolicy taxPolicy = new ExampleTax();

        ProductData product = new ProductData(Id.generate(), new Money(10d), "Product1", ProductType.DRUG, new Date());
        int quantity = 7;
        Money net = new Money(15d);
        Tax tax = taxPolicy.calculateTax(product.getType(), net);
        invoice.addItem(new InvoiceLine(product, quantity, net, tax));

        product = new ProductData(Id.generate(), new Money(5d), "Product1", ProductType.DRUG, new Date());
        quantity = 10;
        net = new Money(10d);
        tax = taxPolicy.calculateTax(product.getType(), net);
        invoice.addItem(new InvoiceLine(product, quantity, net, tax));

        product = new ProductData(Id.generate(), new Money(4.5d), "Product1", ProductType.DRUG, new Date());
        quantity = 22;
        net = new Money(5d);
        tax = taxPolicy.calculateTax(product.getType(), net);
        invoice.addItem(new InvoiceLine(product, quantity, net, tax));

        assertThat(invoice.getItems().size(), is(3));
        assertThat(invoice.getGros(), is(new Money(31.5)));
        assertThat(invoice.getNet(), is(new Money(30.0)));
    }
}
