package pl.uwr.beat_store.ui.profile

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.itextpdf.kernel.colors.Color
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfDocumentInfo
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.AreaBreak
import com.itextpdf.layout.element.LineSeparator
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Text
import com.itextpdf.layout.property.TextAlignment
import pl.uwr.beat_store.R
import pl.uwr.beat_store.data.models.Song
import pl.uwr.beat_store.ui.cart.CartViewModel
import pl.uwr.beat_store.viewmodels.LoggedInViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var userTextView: TextView;
    private lateinit var logoutButton: Button;
    private lateinit var generatePdfButton: Button
    private lateinit var openPdfButton: Button
    private lateinit var loggedInViewModel: LoggedInViewModel

    lateinit var cartViewModel: CartViewModel
    private var  purchasedList = ArrayList<Song>();
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        profileViewModel =
                ViewModelProvider(this).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        profileViewModel.text.observe(viewLifecycleOwner, Observer {
            //textView.text = it
        })


        cartViewModel =
                ViewModelProvider(this).get(CartViewModel::class.java)
        cartViewModel.getCartLiveData().observe(viewLifecycleOwner, {
            purchasedList = it;
            onViewCreated(requireView(), savedInstanceState)
        })

        loggedInViewModel = ViewModelProviders.of(this).get(LoggedInViewModel::class.java);
        logoutButton = root.findViewById(R.id.logout_button)
        generatePdfButton = root.findViewById(R.id.generatepdf_button)
        openPdfButton = root.findViewById(R.id.openepdf_button);
        userTextView = root.findViewById(R.id.textProfile);

        userTextView.text = "Welcome back, " + Firebase.auth.currentUser.email + "!"
        val PERMISSIONS = arrayOf<String>(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        )


        ActivityCompat.requestPermissions(requireActivity(),
                PERMISSIONS,
                1)

        logoutButton.setOnClickListener {
            loggedInViewModel.logOut();
            root.findNavController().navigate(R.id.action_navigation_profile_to_loginRegisterFragment);
            Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
        };

        generatePdfButton.setOnClickListener {
            createLicensePdf();
            Toast.makeText(context, "License sucessfully created", Toast.LENGTH_SHORT).show()
        }

        openPdfButton.setOnClickListener {
            val pdfPath: String = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
            val pdfFile = File(pdfPath, "beatlicense.pdf")
            if (pdfFile.exists()) {
                openPDFDocument(requireContext())
            } else {
                Toast.makeText(context, "You have to generate pdf first!", Toast.LENGTH_SHORT).show();
            }

        }
        return root
    }

    private fun openPDFDocument(context: Context) {
        //Create PDF Intent
        val pdfPath: String = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        val pdfFile = File(pdfPath, "beatlicense.pdf")

        //val pdfFile = File(Environment.getExternalStorageDirectory().absolutePath + "/" + filename)
        val pdfIntent = Intent(Intent.ACTION_VIEW)
        //pdfIntent.setDataAndType(Uri.fromFile(pdfFile), "application/pdf")
        pdfIntent.setDataAndType(FileProvider.getUriForFile(requireContext(), context.applicationContext.packageName + ".provider", pdfFile), "application/pdf");
        //pdfIntent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY

        pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        //Create Viewer Intent
        val viewerIntent = Intent.createChooser(pdfIntent, "Open PDF")
        context.startActivity(viewerIntent)
    }

    private fun createLicensePdf() {
        val pdfPath: String = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        val file = File(pdfPath, "beatlicense.pdf") //TODO: zrob wiele stron w tym pdfie na kazda licencje
        val outputstream: OutputStream = FileOutputStream(file)
        val writer = PdfWriter(file)
        val pdfDocument = PdfDocument(writer)
        val document = Document(pdfDocument)
        val info: PdfDocumentInfo = pdfDocument.documentInfo
        info.title = "Beat"
        info.author = "beat-store"
        info.subject = "Licenses"
        info.keywords = "beat, license"
        info.creator = "beat-store"


        val mColorAccent: Color = DeviceRgb(141, 37, 69)
        val mColorBlack: Color = DeviceRgb(0, 0, 0)
        val mHeadingFontSize = 20.0f
        val mValueFontSize = 18.0f

        val font = PdfFontFactory.createFont("res/font/brandon_medium.otf", "UTF-8", true)

        val useremail=  Firebase.auth.currentUser.email;

        for ((i, x) in purchasedList.withIndex()) //create pdf //TODO: zrob to w petli
        {
            if (i!=0)
            {
                document.add(AreaBreak()) //Next page
            }

            val lineSeparator = LineSeparator(DottedLine())
            lineSeparator.strokeColor = DeviceRgb(0, 0, 68)
            // Title Order Details...
            // Adding Title....
            val mOrderDetailsTitleChunk: Text = Text("Beat-store license").setFont(font).setFontSize(36.0f).setFontColor(mColorBlack)
            val mOrderDetailsTitleParagraph: Paragraph = Paragraph(mOrderDetailsTitleChunk)
                    .setTextAlignment(TextAlignment.CENTER)
            document.add(mOrderDetailsTitleParagraph)

            // Fields of Order Details...
            val sdf = SimpleDateFormat("dd/M/yyyy")
            val currentDate = sdf.format(Date())
            val mOrderDateValueChunk: Text = Text(currentDate).setFont(font).setFontSize(16.0f).setFontColor(mColorBlack)
            val mOrderDateValueParagraph = Paragraph(mOrderDateValueChunk)
            document.add(mOrderDateValueParagraph)
            document.add(lineSeparator)
            document.add(Paragraph(""))

            // Adding Line Breakable Space....

            document.add(Paragraph(""))

            // Fields of Order Details...
            // Adding Chunks for Title and value
            val mOrderIdChunk: Text = Text("License ID").setFont(font).setFontSize(mHeadingFontSize).setFontColor(mColorAccent)
            val mOrderIdParagraph = Paragraph(mOrderIdChunk)
            document.add(mOrderIdParagraph)
            val mOrderIdValueChunk: Text = Text("#"+((100000..999999).random()).toString()).setFont(font).setFontSize(mValueFontSize).setFontColor(mColorBlack) //in future it will generate an unique id
            val mOrderIdValueParagraph = Paragraph(mOrderIdValueChunk)
            document.add(mOrderIdValueParagraph)


            // Fields of Order Details...
            val mOrderAcNameChunk: Text = Text("License owner:").setFont(font).setFontSize(mHeadingFontSize).setFontColor(mColorAccent)
            val mOrderAcNameParagraph = Paragraph(mOrderAcNameChunk)
            document.add(mOrderAcNameParagraph)
            val mOrderAcNameValueChunk: Text = Text(Firebase.auth.currentUser.email).setFont(font).setFontSize(mValueFontSize).setFontColor(mColorBlack)
            val mOrderAcNameValueParagraph = Paragraph(mOrderAcNameValueChunk)
            document.add(mOrderAcNameValueParagraph)
            document.add(Paragraph(""))
            document.add(lineSeparator)

            //name
            val mOrderAcNameChunk1: Text = Text("Beat name").setFont(font).setFontSize(mHeadingFontSize).setFontColor(mColorAccent)
            val mOrderAcNameParagraph1 = Paragraph(mOrderAcNameChunk1)
            document.add(mOrderAcNameParagraph1)
            val mOrderAcNameValueChunk1: Text = Text(x.name).setFont(font).setFontSize(mValueFontSize).setFontColor(mColorBlack)
            val mOrderAcNameValueParagraph1 = Paragraph(mOrderAcNameValueChunk1)
            document.add(mOrderAcNameValueParagraph1)
            document.add(Paragraph(""))
            document.add(lineSeparator)

            val mOrderAcNameChunk2: Text = Text("Produced by").setFont(font).setFontSize(mHeadingFontSize).setFontColor(mColorAccent)
            val mOrderAcNameParagraph2 = Paragraph(mOrderAcNameChunk2)
            document.add(mOrderAcNameParagraph2)
            val mOrderAcNameValueChunk2: Text = Text(x.producer).setFont(font).setFontSize(mValueFontSize).setFontColor(mColorBlack)
            val mOrderAcNameValueParagraph2 = Paragraph(mOrderAcNameValueChunk2)
            document.add(mOrderAcNameValueParagraph2)
            document.add(Paragraph(""))
            document.add(lineSeparator)

            val mOrderAcNameChunk3: Text = Text("Price").setFont(font).setFontSize(mHeadingFontSize).setFontColor(mColorAccent)
            val mOrderAcNameParagraph3 = Paragraph(mOrderAcNameChunk3)
            document.add(mOrderAcNameParagraph3)
            val mOrderAcNameValueChunk3: Text = Text(x.price.toString()).setFont(font).setFontSize(mValueFontSize).setFontColor(mColorBlack)
            val mOrderAcNameValueParagraph3 = Paragraph(mOrderAcNameValueChunk3)
            document.add(mOrderAcNameValueParagraph3)
            document.add(Paragraph(""))
            document.add(lineSeparator)

            val mOrderAcNameChunk4: Text = Text("Download link").setFont(font).setFontSize(mHeadingFontSize).setFontColor(mColorAccent)
            val mOrderAcNameParagraph4 = Paragraph(mOrderAcNameChunk4)
            document.add(mOrderAcNameParagraph4)
            val mOrderAcNameValueChunk4: Text = Text(x.url).setFont(font).setFontSize(mValueFontSize).setFontColor(mColorBlack)
            val mOrderAcNameValueParagraph4 = Paragraph(mOrderAcNameValueChunk4)
            document.add(mOrderAcNameValueParagraph4)
            document.add(Paragraph(""))
            document.add(lineSeparator)

            document.add(Paragraph(""))

        }

        document.close()
        Toast.makeText(context, "Pdf with licenses successfully created, check downloads folder!", Toast.LENGTH_LONG).show();
    }

}