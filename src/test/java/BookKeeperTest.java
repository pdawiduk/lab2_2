import org.junit.Test;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.invoicing.*;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by Shogun on 2016-06-16.
 */
public class BookKeeperTest {

    @Test
    public final void testIssuance_grosAndNetCalculation() {
        InvoiceFactory invoiceFactory = new InvoiceFactory();
        BookKeeper bookKeeper = new BookKeeper(invoiceFactory);
        ClientData client = new ClientData(Id.generate(), "New Client");
        InvoiceRequest request = new InvoiceRequest(client);

        ProductData product = new ProductData(Id.generate(), new Money(10d), "Product1", ProductType.DRUG, new Date());
        RequestItem item = new RequestItem(product, 100, new Money(123.5d));
        request.add(item);

        product = new ProductData(Id.generate(), new Money(2.5), "Product2", ProductType.FOOD, new Date());
        item = new RequestItem(product, 100, new Money(13.6d));
        request.add(item);

        product = new ProductData(Id.generate(), new Money(22.5), "Product3", ProductType.FOOD, new Date());
        item = new RequestItem(product, 100, new Money(133.63d));
        request.add(item);

        System.out.println(request.getClientData());
        Invoice invoice = bookKeeper.issuance(request, new ExampleTax());

        assertThat(invoice.getItems().size(), is(3));
        assertThat(invoice.getGros(), is(new Money(287.21)));
        assertThat(invoice.getNet(), is(new Money(270.73)));

    }
}
